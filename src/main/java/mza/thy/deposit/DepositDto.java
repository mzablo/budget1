package mza.thy.deposit;

import lombok.*;
import mza.thy.domain.Deposit;
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
    private Integer period = 1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String bankName;
    private String description;

    Deposit convert() {
        return Deposit.builder()
                .id(id)
                .amount(amount)
                .percent(percent)
              //  .period(period)
                .date(date)
                .bank(bankName)
                .description(description)
                .build();
    }

    static DepositDto convert(Deposit in) {
        return DepositDto.builder()
                .id(in.getId())
                .amount(in.getAmount())
                .percent(in.getPercent())
                .period(1)//!!
                .date(in.getDate())
                .bankName(in.getBank())
                .description(in.getDescription())
                .build();
    }
}
