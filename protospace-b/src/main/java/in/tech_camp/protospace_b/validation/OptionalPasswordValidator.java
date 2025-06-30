package in.tech_camp.protospace_b.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OptionalPasswordValidator implements ConstraintValidator<ValidOptionalPassword, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return true; // 空はOK（変更なし）
        }
        return password.length() >= 6;
    }
}
