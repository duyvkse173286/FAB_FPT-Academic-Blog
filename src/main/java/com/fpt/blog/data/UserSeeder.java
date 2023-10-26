package com.fpt.blog.data;

import com.fpt.blog.entities.User;
import com.fpt.blog.enums.Role;
import com.fpt.blog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Import initialize user data
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setEmail("admin@fpt.edu.vn");
            admin.setPassword(passwordEncoder.encode("Admin123@"));
            admin.setRole(Role.ADMIN);
            admin.setName("Administrator");
            admin.setDescription("System administrator");

            User mentor = new User();
            mentor.setEmail("mentor@fpt.edu.vn");
            mentor.setPassword(passwordEncoder.encode("Mentor123@"));
            mentor.setRole(Role.MENTOR);
            mentor.setName("Mentor");
            mentor.setDescription("Mentor");

            userRepository.saveAll(Arrays.asList(admin, mentor));
        }

    }
}
