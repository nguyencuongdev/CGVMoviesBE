package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, length = 255)
    String name;

    @Column(nullable = false)
    boolean status;

    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "roles_ID"), // name of field foreign key to join to table accounts
            inverseJoinColumns = @JoinColumn(name = "permissions_ID")// name of field foreign key to join to table roles
    )
    private Set<Permission> permissions;

    @Column(nullable = false)
    String note;
}
