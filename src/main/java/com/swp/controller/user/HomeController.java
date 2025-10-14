package com.swp.controller.user;

import com.swp.dto.request.ChangePasswordRequest;
import com.swp.entity.ProductEntity;
import com.swp.service.ProductService;
import com.swp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/home")
    public String home(Model model) {
        List<ProductEntity> allProducts = productService.getAllProducts();

        List<ProductEntity> top9Products = allProducts.stream()
                .limit(9)
                .toList();

        model.addAttribute("products", top9Products);

        return "home";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @ModelAttribute("changePasswordRequest") ChangePasswordRequest request,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra validation errors
        if (bindingResult.hasErrors()) {
            return "change-password";
        }

        try {
            String username = authentication.getName();
            userService.changePassword(username, request);

            // Dùng FlashAttribute để show message sau redirect
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đổi mật khẩu thành công! Vui lòng đăng nhập lại với mật khẩu mới.");
            return "redirect:/change-password"; // redirect về chính form hoặc login
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "change-password";
        }
    }

}
