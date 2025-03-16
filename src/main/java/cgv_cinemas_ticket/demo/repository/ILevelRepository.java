package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILevelRepository extends JpaRepository<Level, Long> {
}
