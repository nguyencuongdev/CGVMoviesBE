package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleRepository extends JpaRepository<Role, Long> {
}
