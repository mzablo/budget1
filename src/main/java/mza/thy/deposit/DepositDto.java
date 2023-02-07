package mza.thy.deposit;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DepositDto {
    private Long id;
    private BigDecimal amount = BigDecimal.ZERO;
    private BigDecimal percent = BigDecimal.ONE;
    private String period = DepositPeriod.ONE_MONTH;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    private LocalDate date = LocalDate.now();
    private LocalDate endDate;
    private BigDecimal interest;
    private BigDecimal interestNet;
    private Boolean prolonged;
    private BigDecimal totalAmount;
    private Boolean active;
    private String bankName;
    private String description;

    Deposit convert() {
        return new Deposit(amount, date, description, percent, period, bankName, active, prolonged);
    }

    static DepositDto convert(Deposit in) {
        if (in.getId()==954){
            log.debug("mza!! "+in.getId());
        }
        return DepositDto.builder()
                .id(in.getId())
                .amount(in.getAmount())
                .percent(in.getPercent())
                .period(in.getPeriod())
                .date(in.getDate())
                .endDate(in.getEndDate())
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
