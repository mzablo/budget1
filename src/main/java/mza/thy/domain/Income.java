package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private BigDecimal amount;

    @Column(name = "income_date")
    private LocalDate date;

    @Column(name = "income_year")
    private Integer year;

    @Column(name = "income_month")
    private Integer month;
    @Setter
    private String description;
    @Setter
    private Long operationId;

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
