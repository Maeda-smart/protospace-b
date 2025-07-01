package in.tech_camp.protospace_b.custom_user;

import java.util.Collection;
import java.util.Collections;
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
  public Collection<? extends GrantedAuthority> getAuthorities() {
      String role = user.getRoleName();
      if (role == null || role.trim().isEmpty()) {
          System.out.println("警告：ユーザー「" + user.getNickname() + "」にロールが設定されていません。");
          return Collections.emptyList();
      }
      return List.of(new SimpleGrantedAuthority(role.trim()));
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
