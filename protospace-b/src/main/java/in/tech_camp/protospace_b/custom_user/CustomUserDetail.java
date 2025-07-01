package in.tech_camp.protospace_b.custom_user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.tech_camp.protospace_b.entity.UserEntity;
import lombok.Data;

@Data
public class CustomUserDetail implements UserDetails {
  private final UserEntity user;
  
  public CustomUserDetail(UserEntity user){
    this.user = user;
  }

  public Integer getId(){
    return user.getId();
  }

  @Override
  public String getUsername(){
    return user.getNickname();
  }

  @Override
  public String getPassword(){
    return user.getPassword();
  }

  // ユーザー管理設定
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities(){
    // ユーザーのロールを権限として返す（ROLE_ADMIN や ROLE_USER）
    return List.of(new SimpleGrantedAuthority(user.getRole()));

    // return Collections.emptyList();
  }


  @Override
  public boolean isEnabled() {
    return true;
  }
}
