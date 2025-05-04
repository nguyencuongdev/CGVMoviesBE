package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Level;
import cgv_cinemas_ticket.demo.model.Popcom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ILevelRepository extends JpaRepository<Level, Long> {
    List<Level> findAllByNameContainingIgnoreCase(String name);
}
