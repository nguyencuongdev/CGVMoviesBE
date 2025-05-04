package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "popcoms")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Popcom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 255, unique = true)
    String name;
    @Column(nullable = false, length = 255)
    String srcImg;
    @Column(nullable = false)
    int price;
    @Column(nullable = false)
    boolean status;
}
