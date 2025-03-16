package cgv_cinemas_ticket.demo.validation;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {PhoneNumberValidator.class}
)
public @interface PhoneNumberUniqueConstraint {
    String message() default "Phone number already exists in a account";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
}
