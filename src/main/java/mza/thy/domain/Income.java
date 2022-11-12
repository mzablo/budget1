package mza.thy.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    private BigDecimal amount = BigDecimal.ZERO;
    private LocalDate date;
    private Integer year;
    private Integer month;

    @Setter
    private String name;

    @Setter
    private String description;

    @Setter
    private Long operationId;

    public void setDate(LocalDate date) {
        this.date = date;
        setMonth(date);
        setYear(date);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = Optional.ofNullable(amount).orElse(BigDecimal.ZERO);
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
