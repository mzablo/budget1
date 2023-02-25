package mza.thy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "lokata")
@Getter
@NoArgsConstructor
@ToString
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount = BigDecimal.ZERO;
    private LocalDate date;

    private Integer year;
    private Integer month;
    private LocalDate endDate;
    private BigDecimal interest;
    private BigDecimal interestNet;
    private BigDecimal totalAmount;
    private String period = DepositPeriod.ONE_MONTH;
    private BigDecimal percent;

    @Setter
    private String description;

    @Setter
    private String bank;

    @Setter
    private Boolean active = true;

    @Setter
    @Column(name = "prolongated", nullable = false)
    private Boolean prolonged;

    public Deposit update(BigDecimal amount, LocalDate date, BigDecimal interest, String description, BigDecimal percent, String period, String bank, Boolean active, Boolean prolonged) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.percent = percent;
        this.period = period;
        this.bank = bank;
        this.active = active;
        this.prolonged = prolonged;
        recalculate();
        return this;
    }

    public Deposit(BigDecimal amount, LocalDate date, String description, BigDecimal percent, String period, String bank, Boolean active, Boolean prolonged) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.percent = percent;
        this.period = period;
        this.bank = bank;
        this.active = active;
        this.prolonged = prolonged;
        recalculate();
    }

    private void recalculate() {
        setMonth(date);
        setYear(date);
        setEndDate();
        calculateInterest();
        calculateInterestNet();
        calculateTotalAmount();
    }

    private void setEndDate() {
        if (Objects.nonNull(date)) {
            this.endDate = date.plusMonths(DepositPeriod.getMonths(period));
        }
    }

    private void setMonth(LocalDate date) {
        month = Optional.ofNullable(date).map(LocalDate::getMonthValue)
                .orElse(null);
    }

    private void setYear(LocalDate date) {
        year = Optional.ofNullable(date).map(LocalDate::getYear)
                .orElse(null);
    }

    void calculateTotalAmount() {
        totalAmount = amount.add(interestNet);
    }

    private void calculateInterestNet() {
        var belkaValue = interest.multiply(BigDecimal.valueOf(0.19));
        interestNet = interest.subtract(belkaValue);
    }

    private void calculateInterest() {
        interest = amount.multiply(percent)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(DepositPeriod.getMonths(period)));
    }
}