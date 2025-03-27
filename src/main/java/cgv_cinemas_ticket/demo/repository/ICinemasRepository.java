package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Cinemas;
import cgv_cinemas_ticket.demo.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICinemasRepository extends JpaRepository<Cinemas, Long> {
    List<Cinemas> findAllByTheater(Theater theater);
}
