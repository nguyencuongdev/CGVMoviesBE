package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ITheaterRepository extends JpaRepository<Theater, Long> {
    boolean existsByName(String name);
    boolean existsByAddress(String address);
    boolean existsByNameAndIdNot(String name, Long id);
    boolean existsByAddressAndIdNot(String address, Long id);
}
