package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.MovieCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IMovieCategoryRepository extends JpaRepository<MovieCategory, Long> {
    List<MovieCategory> findAllByNameContainingIgnoreCase(String name);
}
