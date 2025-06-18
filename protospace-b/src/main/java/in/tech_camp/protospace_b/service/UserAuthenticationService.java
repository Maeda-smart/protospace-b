package in.tech_camp.protospace_b.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.UserLoginRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {
  private final UserLoginRepository userLoginRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
    UserEntity userEntity = userLoginRepository.findByEmail(email);
    if(userEntity == null){
      throw new UsernameNotFoundException("User not found with email: " + email);
    }
    
    return new CustomUserDetail(userEntity);
  }
}
