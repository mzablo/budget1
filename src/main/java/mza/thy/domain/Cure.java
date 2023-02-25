package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String medicine;
    private BigDecimal price = BigDecimal.ZERO;
    @Column(name = "descr")
    private String description;
    private String dose;
    @Column(name = "id_il")
    private Long illnessId;
}
