package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

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
    private LocalDate date;
    private String medicine;
    private BigDecimal price = BigDecimal.ZERO;
    @Column(name = "descr")
    private String description;
    private String dose;
    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "illness_id")
    private Long illnessId;
}
