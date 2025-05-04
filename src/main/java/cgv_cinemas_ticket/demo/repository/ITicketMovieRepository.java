package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.TicketMovie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITicketMovieRepository extends JpaRepository<TicketMovie, Long> {
    List<TicketMovie> findAllByNameContainingIgnoreCase(String name);
}
