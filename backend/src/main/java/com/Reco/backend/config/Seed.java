package com.Reco.backend.config;

import com.Reco.backend.model.Role;
import com.Reco.backend.model.User;
import com.Reco.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Seed implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Seed.class);

    private static final int GENERATED_PASSWORD_BYTES = 18;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminEmail;
    private final String adminUsername;
    private final String configuredAdminPassword;

    public Seed(UserRepository userRepository,
                PasswordEncoder passwordEncoder,
                @Value("${app.seed.admin.email}") String adminEmail,
                @Value("${app.seed.admin.username}") String adminUsername,
                @Value("${app.seed.admin.password:}") String configuredAdminPassword) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminEmail = adminEmail;
        this.adminUsername = adminUsername;
        this.configuredAdminPassword = configuredAdminPassword;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() != 0) {
            return;
        }

        boolean generated = configuredAdminPassword == null || configuredAdminPassword.isBlank();
        String rawPassword = generated ? generatePassword() : configuredAdminPassword;

        User admin = User.builder()
                .firstName("Admin")
                .lastName("Man")
                .username(adminUsername)
                .email(adminEmail)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);

        if (generated) {
            log.warn("Seeded admin {} with a generated password: {}. "
                            + "Set RECO_ADMIN_PASSWORD to choose your own.",
                    adminEmail, rawPassword);
        } else {
            log.info("Seeded admin {} using the password from RECO_ADMIN_PASSWORD.", adminEmail);
        }
    }

    private String generatePassword() {
        byte[] bytes = new byte[GENERATED_PASSWORD_BYTES];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}