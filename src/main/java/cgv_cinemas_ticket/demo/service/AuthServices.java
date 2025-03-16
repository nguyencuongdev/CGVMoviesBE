package cgv_cinemas_ticket.demo.service;

import cgv_cinemas_ticket.demo.model.Account;
import cgv_cinemas_ticket.demo.repository.IAccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthServices {
    IAccountRepository accountRepository;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
