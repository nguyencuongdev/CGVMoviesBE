package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "theaters_images")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TheaterImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 255, unique = true)
    String srcImg;
    @Column(nullable = false, length = 255, unique = true)
    String fileName;
    @ManyToOne
    @JoinColumn(name = "theaters_ID", nullable = false)
    Theater theater;
}
