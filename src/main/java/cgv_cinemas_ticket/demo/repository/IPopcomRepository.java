package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Popcom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPopcomRepository extends JpaRepository<Popcom, Long> {
}
