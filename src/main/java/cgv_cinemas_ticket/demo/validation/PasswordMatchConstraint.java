package cgv_cinemas_ticket.demo.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // validator on a object
@Retention(RetentionPolicy.RUNTIME) // on runtime
@Constraint(
        validatedBy = {PasswordMatchValidator.class}
)
public @interface PasswordMatchConstraint {
    String message() default "Confirm password not matching with password!";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
