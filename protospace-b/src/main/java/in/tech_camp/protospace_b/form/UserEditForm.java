package in.tech_camp.protospace_b.form;

// import org.hibernate.validator.constraints.Length;
import org.springframework.validation.BindingResult;

import in.tech_camp.protospace_b.validation.ValidOptionalPassword;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
import in.tech_camp.protospace_b.validation.ValidationPriority2;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEditForm {
  private Integer id;
  
  @NotBlank(message = "Nickname can't be blank", groups = ValidationPriority1.class)
  private String nickname;

  @NotBlank(message = "Email can't be blank", groups = ValidationPriority1.class)
  @Email(message = "Email should be valid", groups = ValidationPriority2.class)
  private String email;

  @ValidOptionalPassword(groups = ValidationPriority2.class)
  private String password;

  // 再入力したパスワード
  private String passwordConfirmation;

  // プロフィール
  @NotBlank(message = "Profile can't be blank", groups = ValidationPriority1.class)
  private String profile;

  // 所属
  @NotBlank(message = "Affiliation can't be blank", groups = ValidationPriority1.class)
  private String affiliation;
  
  // 役職
  @NotBlank(message = "Position can't be blank", groups = ValidationPriority1.class)
  private String position;

  // 2つのパスワードが一致しているか確認
  public void validatePasswordConfirmation(BindingResult result) {
      if (!password.equals(passwordConfirmation)) {
          result.rejectValue("passwordConfirmation", "error.user", "Password confirmation doesn't match Password");
      }
  }
}
