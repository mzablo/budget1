package mza.thy.deposit;

import lombok.*;
import mza.thy.domain.Deposit;
import mza.thy.domain.DepositPeriod;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DepositDto {
    private Long id;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal percent = BigDecimal.ONE;
    private String period = DepositPeriod.ONE_MONTH;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    private LocalDate date = LocalDate.now();
    @Builder.Default
    private LocalDate endDate = LocalDate.now().plusMonths(1);
    private BigDecimal interest;
    private BigDecimal interestNet;
    private Boolean prolonged;
    private BigDecimal totalAmount;
    private Boolean active;
    private String bankName;
    private String description;

    Deposit convert() {
        return new Deposit(amount, date, interest, description, percent, period, bankName, active, prolonged);
    }

    static DepositDto convert(Deposit in) {
        return DepositDto.builder()
                .id(in.getId())
                .amount(in.getAmount())
                .percent(in.getPercent())
                .period(in.getPeriod())
                .date(in.getDate())
                .bankName(in.getBank())
                .description(in.getDescription())
                .interest(in.getInterest())
                .interestNet(in.getInterestNet())
                .totalAmount(in.getTotalAmount())
                .prolonged(in.getProlonged())
                .active(in.getActive())
                .build();
    }
}
