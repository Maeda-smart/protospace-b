package in.tech_camp.protospace_b.form;

import org.springframework.validation.BindingResult;

import lombok.Data;

@Data
public class UserForm {
  private Integer id;
  private String nickname;
  private String email;
  private String password;

  // 再入力したパスワード
  private String passwordConfirmation;

  // プロフィール
  private String profile;

  // 所属
  private String affiliation;
  
  // 役職
  private String position;

  // 2つのパスワードが一致しているか確認
  public void validatePasswordConfirmation(BindingResult result) {
      if (!password.equals(passwordConfirmation)) {
          result.rejectValue("passwordConfirmation", null, "Password confirmation doesn't match Password");
      }
  }
}
