package com.hortifruti.config;

import com.hortifruti.model.User;
import com.hortifruti.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminSeed implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeed(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Optional<User> existing = userRepository.findByEmail("admin@hortiflow.com");
        if (existing.isEmpty()) {
            User u = new User();
            u.setName("Admin");
            u.setEmail("admin@hortiflow.com");
            u.setPasswordHash(passwordEncoder.encode("admin123"));
            u.setRole("ADMIN");
            u.setActive(true);
            userRepository.save(u);
            System.out.println("✅ Usuário admin criado: admin@hortiflow.com / admin123");
        }
    }
}
