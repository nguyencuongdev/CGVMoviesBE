package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    @Column(nullable = true, length = 255)
    String address;
    @Column(nullable = false)
    boolean status;
    @Column(nullable = false)
    String hotline;
    String fax;
    @Column(nullable = false, length = 255)
    String linkMap;
    @Column(nullable = true)
    String note;

    @OneToMany
    @JoinColumn(name = "theaters_ID",nullable = false)
    @ToString.Exclude
    Set<TheaterImage> images;
}
