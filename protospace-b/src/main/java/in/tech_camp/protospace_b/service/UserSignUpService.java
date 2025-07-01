package in.tech_camp.protospace_b.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.UserSignUpRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserSignUpService {
    private final UserSignUpRepository userSignUpRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUserWithEncryptedPassword(UserEntity userEntity) {
        String encodedPassword = encodePassword(userEntity.getPassword());
        userEntity.setPassword(encodedPassword);
        userSignUpRepository.insert(userEntity);
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
