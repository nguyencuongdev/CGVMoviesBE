package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.TicketMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITicketMovieRepository extends JpaRepository<TicketMovie, Long> {
}
