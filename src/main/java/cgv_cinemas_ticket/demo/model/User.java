package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 255)
    String name;

    @Column(nullable = true, length = 255)
    String avatar;

    @Column(nullable = true)
    String faviousCinemas;

    @Column(nullable = false)
    Date dateOfBirth;

    @Column(nullable = false)
    boolean gender;

    @CreatedDate
    Date createAt;

    @LastModifiedDate
    Date updateAt;
}
