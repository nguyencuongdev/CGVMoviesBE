package cgv_cinemas_ticket.demo.validation;

import cgv_cinemas_ticket.demo.dto.request.AccountSignupRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatchConstraint, AccountSignupRequest> {

    @Override
    public void initialize(PasswordMatchConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AccountSignupRequest accountSignup, ConstraintValidatorContext context) {
        if (accountSignup.getPassword() == null || accountSignup.getConfirmPassword() == null) {
            return false;
        }
        return accountSignup.getPassword().equals(accountSignup.getConfirmPassword());
    }
}
