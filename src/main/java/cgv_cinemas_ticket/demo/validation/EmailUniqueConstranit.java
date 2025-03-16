package cgv_cinemas_ticket.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Where are annotation apply ?(TH này là field)
@Target(ElementType.FIELD)
// when is annotation handling ?
@Retention(RetentionPolicy.RUNTIME)
// class handling validate for this constraint
@Constraint(
        validatedBy = {EmailUniqueValidator.class}
)
public @interface EmailUniqueConstranit {
    // khi define mot custom anotation validation cần 3 field: message, groups, payload;
    String message() default "An account already exists with the this email!";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
