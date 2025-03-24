package cgv_cinemas_ticket.demo.model;

import cgv_cinemas_ticket.demo.dto.request.FileInfoRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "cinemas_types")
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemasType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String avatar;
    boolean status;
    String note;

    @CreatedDate
    Date createAt;
    @LastModifiedDate
    Date updateAt;
}
