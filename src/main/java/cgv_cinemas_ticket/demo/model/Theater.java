package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "theaters")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 255, unique = true)
    String name;
    @Column(nullable = false, length = 255)
    String city;
    @Column(nullable = false, length = 255)
    String address;
    @Column(nullable = false)
    boolean status;
    @Column(nullable = false, length = 10)
    String hotline;
    @Column(nullable = false, length = 10)
    String fax;
    @Column(nullable = false, length = 255)
    String linkMap;
    @CreatedDate
    Date createAt;
    @LastModifiedDate
    Date updateAt;
    @Column(nullable = true)
    String note;

    @OneToMany(mappedBy = "theater",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    Set<TheaterImage> images;
}
