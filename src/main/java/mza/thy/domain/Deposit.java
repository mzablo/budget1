package mza.thy.domain;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "lokata")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private BigDecimal amount = BigDecimal.ZERO;
    private LocalDate date;
    private Integer year;
    private Integer month;
    private LocalDate endDate;
    private BigDecimal interest;
    private BigDecimal interestNet;
    private BigDecimal totalAmount;
    @Setter
    private String description;

    @Setter
    private BigDecimal percent;

  //  @Setter
  //  private Integer period = 1;//periodInMonths

    @Setter
    private String bank;

    @Setter
    private Boolean active = true;

    @Setter
    private Boolean prolongated;

    public void setDate(LocalDate date) {
        this.date = date;
        setMonth(date);
        setYear(date);
  //      setEndDate();
    }
/*
    public void setPeriod(Integer period) {
        this.period = period;
        setEndDate();
    }

    private void setEndDate() {
        if (Objects.nonNull(date)) {
            endDate = date.plusMonths(period);
        }
    }
*/
    private void setMonth(LocalDate date) {
        month = Optional.ofNullable(date).map(LocalDate::getMonthValue)
                .orElse(null);
    }

    private void setYear(LocalDate date) {
        year = Optional.ofNullable(date).map(LocalDate::getYear)
                .orElse(null);
    }
}

