package mza.thy.income;

import lombok.*;
import mza.thy.domain.Income;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter//na potrzeby thymeleaf
@Getter
@ToString
public class IncomeDto {
    private Long id;
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Integer year;
    private Integer month;
    private String description;
    private Long operationId;

    Income convert() {
        return Income.builder()
                .id(id)
                .amount(amount)
                .date(date)
                .year(year)
                .month(month)
                .description(description)
                .operationId(operationId)
                .build();
    }

    static IncomeDto convert(Income in) {
        return IncomeDto.builder()
                .id(in.getId())
                .amount(in.getAmount())
                .date(in.getDate())
                .year(in.getYear())
                .month(in.getMonth())
                .description(in.getDescription())
                .operationId(in.getOperationId())
                .build();
    }
}
