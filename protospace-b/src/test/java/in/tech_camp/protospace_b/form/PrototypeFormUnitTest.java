package in.tech_camp.protospace_b.form;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.BindingResult;

import com.github.javafaker.Faker;

import in.tech_camp.protospace_b.factory.PrototypeFormFactory;
import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ActiveProfiles("test")
public class PrototypeFormUnitTest {

    private PrototypeForm prototypeForm;
    private static final Faker faker = new Faker();
    private Validator validator;
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        prototypeForm = PrototypeFormFactory.createPrototype();
        // prototypeForm = new PrototypeForm();
        bindingResult = Mockito.mock(BindingResult.class);
    }

    @Nested
    class CanSubmitPrototype {

        @Test
        public void SubmitWhenFilledAllInput() {
            assertNotNull(prototypeForm);
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm, ValidationPriority1.class);
            assertEquals(0, violations.size());
        }
    }

    @Nested
    class CannotSubmitPrototype {

        // @Test
        // public void ValidationErrorWhenNicknameIsBlank() {
        //     userForm.setNickname("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Nickname can't be blank", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenEmailIsBlank() {
        //     userForm.setEmail("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Email can't be blank", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenEmailIsNotIncludeAtMark() {
        //     userForm.setEmail("this.is.not.email");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Email should be valid", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenPasswordIsBlank() {
        //     userForm.setPassword("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Password can't be blank", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenPasswordIsShorterThan6Chars() {
        //     userForm.setPassword("5char");
        //     userForm.setPasswordConfirmation(userForm.getPassword());
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority2.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Password must be at least 6 characters long", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenPasswordAndConfirmationAreDifferent() {
        //     userForm.setPasswordConfirmation("DifferentPassword");
        //     userForm.validatePasswordConfirmation(bindingResult);
        //     verify(bindingResult).rejectValue("passwordConfirmation", "error.user",
        //             "Password confirmation doesn't match Password");
        // }

        // @Test
        // public void ValidationErrorWhenProfileIsBlank() {
        //     userForm.setProfile("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Profile can't be blank", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenAffiliationIsBlank() {
        //     userForm.setAffiliation("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Affiliation can't be blank", violations.iterator().next().getMessage());
        // }

        // @Test
        // public void ValidationErrorWhenPositionIsBlank() {
        //     userForm.setPosition("");
        //     Set<ConstraintViolation<UserForm>> violations = validator.validate(userForm, ValidationPriority1.class);
        //     assertEquals(1, violations.size());
        //     assertEquals("Position can't be blank", violations.iterator().next().getMessage());
        // }
    }

}
