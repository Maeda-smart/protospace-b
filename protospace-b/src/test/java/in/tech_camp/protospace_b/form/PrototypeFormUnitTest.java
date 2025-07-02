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
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            assertEquals(0, violations.size());
        }
    }

    @Nested
    class CannotSubmitPrototype {

        @Test
        public void ValidationErrorWhenPrototypeNameIsBlank() {
            prototypeForm.setPrototypeName("");
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("PrototypeName can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void ValidationErrorWhenCatchCopyIsBlank() {
            prototypeForm.setCatchCopy("");
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("CatchCopy can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void ValidationErrorWhenConceptIsBlank() {
            prototypeForm.setConcept("");
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Concept can't be blank", violations.iterator().next().getMessage());
        }

        @Test
        public void ValidationErrorWhenImageIsBlank() {
            prototypeForm.setImgFile(null);
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            assertEquals(1, violations.size());
            assertEquals("Image can't be blank", violations.iterator().next().getMessage());
        }

    }

}
