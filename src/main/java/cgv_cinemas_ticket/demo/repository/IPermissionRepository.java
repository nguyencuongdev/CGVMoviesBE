package cgv_cinemas_ticket.demo.repository;

import cgv_cinemas_ticket.demo.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPermissionRepository extends JpaRepository<Permission, Long> {
}
