package in.tech_camp.protospace_b;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.UserSignUpRepository;

@Configuration
public class AdminConfig {
   @Bean
    public CommandLineRunner initAdmin(UserSignUpRepository UserSignUpRepository, PasswordEncoder encoder) {
        return args -> {
            if (!UserSignUpRepository.existsByEmail("admin@admin.com")) {
                UserEntity admin = new UserEntity();
                admin.setNickname("管理者");
                admin.setEmail("admin@admin.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setProfile("管理者");
                admin.setAffiliation("管理者");
                admin.setPosition("管理者");
                admin.setRoleName("ROLE_ADMIN");
                UserSignUpRepository.insert(admin);
            }
        };
    }
}
