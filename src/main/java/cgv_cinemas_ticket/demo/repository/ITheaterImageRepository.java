package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.TheaterImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITheaterImageRepository extends JpaRepository<TheaterImage, Long> {
}
