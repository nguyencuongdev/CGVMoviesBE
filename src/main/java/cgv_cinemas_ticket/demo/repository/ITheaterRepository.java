package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITheaterRepository extends JpaRepository<Theater, Long> {
}
