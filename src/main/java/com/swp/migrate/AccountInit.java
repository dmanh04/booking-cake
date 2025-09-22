package com.swp.migrate;

import com.swp.entity.RoleEntity;
import com.swp.entity.UserEntity;
import com.swp.repository.RoleRepository;
import com.swp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class AccountInit implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            //admin
            RoleEntity roleadmin = roleRepository.findByName("ADMIN");
            RoleEntity rolestaff = roleRepository.findByName("STAFF");

            UserEntity admin = new UserEntity();
            admin.setEmail("admin@gmail.com");
            admin.setPassword(passwordEncoder.encode("12345"));
            admin.setStatus(Boolean.TRUE);
            admin.setRole(roleadmin);


            //staff
            UserEntity staff = new UserEntity();
            staff.setEmail("staff@gmail.com");
            staff.setPassword(passwordEncoder.encode("12345"));
            staff.setStatus(Boolean.TRUE);
            staff.setRole(rolestaff);

            userRepository.save(admin);
            userRepository.save(staff);
        }
    }
}
