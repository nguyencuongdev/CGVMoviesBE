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
public class EmailUniqueValidator implements ConstraintValidator<EmailUniqueConstranit, String> {
    IAccountRepository accountRepository;

    // method will called when annotation initialized
    // Can you get data's of field validation from its
    @Override
    public void initialize(EmailUniqueConstranit constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    //method handling validation for email
    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
       Account accountExited = accountRepository.findByEmail(email);
        return accountExited == null;
    }
}
