package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.FileTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFileTempRepository extends JpaRepository<FileTemp, Long> {
    FileTemp findByFileName(String fileName);
    void deleteByFileName(String fileName);
}
