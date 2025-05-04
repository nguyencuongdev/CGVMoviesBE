package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.CinemasType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICinemasTypeRepository extends JpaRepository<CinemasType, Long> {
    List<CinemasType> findAllByNameContainingIgnoreCase(String name);
}
