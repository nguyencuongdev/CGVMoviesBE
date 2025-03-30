package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Cinemas;
import cgv_cinemas_ticket.demo.model.Theater;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICinemasRepository extends JpaRepository<Cinemas, Long> {
    Page<Cinemas> findAllByTheater(Theater theater, Pageable pageable);
    int countByTheater(Theater theater);
    List<Cinemas> findByNameContainingIgnoreCaseOrTheater_NameContainingIgnoreCaseOrCinemasType_NameContainingIgnoreCase(
            String name, String theaterName, String cinemasTypeName);
}
