package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);
    Account findByPhoneNumber(String phoneNumber);
    Account findByEmailOrPhoneNumber(String email, String phoneNumber);
}
