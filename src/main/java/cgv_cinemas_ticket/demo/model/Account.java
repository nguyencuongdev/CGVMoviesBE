package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true, length = 255)
    String email;

    @Column(nullable = false, unique = true, length = 10)
    String phoneNumber;
    @Column(nullable = false, length = 255)
    String password;
    @Column(nullable = false)
    boolean status;
    @Column(nullable = false)
    int currentPoint = 0;

    @CreatedDate
    Date createAt;

    @LastModifiedDate
    Date updateAt;

    @OneToOne
    @JoinColumn(name = "users_ID", referencedColumnName = "id")
    User user;

    @OneToOne
    @JoinColumn(name = "levels_ID", referencedColumnName = "id")
    Level level;

    @ManyToMany
    @JoinTable(
            name = "accounts_roles",
            joinColumns = @JoinColumn(name = "accounts_ID"), // name of field foreign key to join to table accounts
            inverseJoinColumns = @JoinColumn(name = "roles_ID")// name of field foreign key to join to table roles
    )
    @ToString.Exclude
    private Set<Role> roles;

    boolean isActive;

    @Column(nullable = true)
    String note;
}
