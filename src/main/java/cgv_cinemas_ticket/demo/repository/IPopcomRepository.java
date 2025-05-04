package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Popcom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPopcomRepository extends JpaRepository<Popcom, Long> {
    List<Popcom> findAllByNameContainingIgnoreCase(String name);
}
