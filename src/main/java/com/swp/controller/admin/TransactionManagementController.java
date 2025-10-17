package com.swp.controller.admin;

import com.swp.entity.PaymentEntity;
import com.swp.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin/transactions")
@RequiredArgsConstructor
public class TransactionManagementController {

    private final PaymentService paymentService;

    @GetMapping
    public String transactionList(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size,
                                   @RequestParam(required = false) String vnpayTxnRef,
                                   @RequestParam(required = false) String status,
                                   ModelMap model) {

        // Validate page size
        if (size <= 0) size = 10;
        if (size > 100) size = 100; // Max 100 items per page
        
        // Get transactions with pagination and filters
        Page<PaymentEntity> transactionPage = paymentService.searchAndFilterPayments(
                vnpayTxnRef, status, page, size);

        // Add data to model
        model.put("transactions", transactionPage.getContent());
        model.put("currentPage", page);
        model.put("totalPages", transactionPage.getTotalPages());
        model.put("totalElements", transactionPage.getTotalElements());
        model.put("size", size);
        model.put("vnpayTxnRef", vnpayTxnRef);
        model.put("status", status);
        
        // Calculate statistics
        long totalTransactions = paymentService.countAll();
        long successCount = paymentService.countByStatus("SUCCESS");
        long failedCount = paymentService.countByStatus("FAILED");
        long pendingCount = paymentService.countByStatus("PENDING");
        
        model.put("totalTransactions", totalTransactions);
        model.put("successCount", successCount);
        model.put("failedCount", failedCount);
        model.put("pendingCount", pendingCount);

        return "admin/transactions";
    }
}

