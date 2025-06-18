package in.tech_camp.protospace_b.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import in.tech_camp.protospace_b.factory.UserFormFactory;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class UserFormUnitTest {

    private UserForm userForm;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        userForm = UserFormFactory.createUser();
    }

    @Nested
    class CanSignUp {

        @Test
        public void SignUpWhenFilledAllInput() {
          Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
          assertEquals(0, violations.size());
        }
    }

    @Nested
    class CannotSignUp {

        @Test
        public void ValidationErrorWhenNicknameIsBlank() {
            userForm.setNickname("");
          Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
          assertEquals(1, violations.size());
        }
    }

}
