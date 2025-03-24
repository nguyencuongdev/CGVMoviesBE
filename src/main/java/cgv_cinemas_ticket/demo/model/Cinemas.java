package cgv_cinemas_ticket.demo.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "cinemas")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cinemas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String avatar;
    boolean status;
    String note;
    int standardChairs;
    int vipChairs;
    int sweetBoxChairs;

    @OneToOne
    @JoinColumn(name = "theaters_ID", nullable = false)
    Theater theater;
    @OneToOne
    @JoinColumn(name = "cinemas_types_ID", nullable = false)
    CinemasType cinemasType;

    @CreatedDate
    Date createAt;
    @LastModifiedDate
    Date updateAt;
}
