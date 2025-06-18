package in.tech_camp.protospace_b.custom_user;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.protospace_b.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {
  private final UserEntity user;
  
  public CustomUserDetail(UserEntity user){
    this.user = user;
  }

  @Override
  public String getUsername(){
    return user.getNickname();
  }

  @Override
  public String getPassword(){
    return user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities(){
    return Collections.emptyList();
  }
}
