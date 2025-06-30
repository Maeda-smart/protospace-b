package in.tech_camp.protospace_b.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = OptionalPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOptionalPassword {
    String message() default "Password must be at least 6 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
