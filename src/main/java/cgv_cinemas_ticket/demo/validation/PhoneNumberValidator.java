package cgv_cinemas_ticket.demo.validation;

import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.repository.IAccountRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberUniqueConstraint, String> {
    IAccountRepository accountRepository;

    @Override
    public void initialize(PhoneNumberUniqueConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Account accountExited = accountRepository.findByPhoneNumber(value);
        return accountExited == null;
    }
}
