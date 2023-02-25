package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Outcome {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private BigDecimal price = BigDecimal.ZERO;

    @Setter
    private Integer counter = 1;
    private LocalDate date;
    private Integer year;
    private Integer month;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    @Column(name = "cathegory")
    private String category;

    @Setter
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_id")
    private Operation operation;

    public void setDate(LocalDate date) {
        this.date = date;
        setMonth(date);
        setYear(date);
    }

    private void setMonth(LocalDate date) {
        month = Optional.ofNullable(date).map(LocalDate::getMonthValue)
                .orElse(null);
    }

    private void setYear(LocalDate date) {
        year = Optional.ofNullable(date).map(LocalDate::getYear)
                .orElse(null);
    }
}
