package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<User, Long> {
}
