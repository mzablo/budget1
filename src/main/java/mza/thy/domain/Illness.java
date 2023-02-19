package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Illness {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Integer year;
    private Integer month;
    private String name;
    private String who;
    @Column(name = "descr")
    private String description;
    private Integer free = 0;
}
