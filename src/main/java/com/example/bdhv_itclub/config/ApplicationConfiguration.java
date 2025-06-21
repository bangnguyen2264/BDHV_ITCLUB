package com.example.bdhv_itclub.config;

import com.example.bdhv_itclub.entity.Role;
import com.example.bdhv_itclub.entity.User;
import com.example.bdhv_itclub.repository.RoleRepository;
import com.example.bdhv_itclub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfiguration {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;



    @Bean
    public CommandLineRunner initApp() {
        return args -> {
            if (!roleRepository.existsByName("ROLE_USER")) {
                roleRepository.save(Role.builder().name("ROLE_USER").build());
            }
            if (!roleRepository.existsByName("ROLE_ADMIN")) {
                roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            }
            if (!roleRepository.existsByName("ROLE_TEACHER")) {
                roleRepository.save(Role.builder().name("ROLE_TEACHER").build());
            }
            log.info("Roles initialized successfully");

            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
            Role teacherRole = roleRepository.findByName("ROLE_TEACHER").orElseThrow();
            Role studentRole = roleRepository.findByName("ROLE_USER").orElseThrow();

            if (!userRepository.existsByEmail("admin@gmail.com")) {
                userRepository.save(
                        User.builder()
                                .fullName("admin")
                                .email("admin@gmail.com")
                                .password(passwordEncoder.encode("admin"))
                                .role(adminRole)
                                .enabled(true)
                                .build()
                );
                log.info("Admin initialized successfully");
            }

            if(!userRepository.existsByEmail("exampleUser@gmail.com")){
                userRepository.save(
                        User.builder()
                                .fullName("Example User")
                                .email("exampleUser@gmail.com")
                                .password(passwordEncoder.encode("exampleUser"))
                                .role(studentRole)
                                .enabled(true)

                                .build()
                );
                log.info("Example user initialized successfully");
            }


        };
    }
}
