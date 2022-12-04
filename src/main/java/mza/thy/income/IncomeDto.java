package mza.thy.income;

import lombok.*;
import mza.thy.domain.Account;
import mza.thy.domain.Income;
import mza.thy.domain.Operation;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter//na potrzeby thymeleaf
@Getter
@ToString
public class IncomeDto {
    private Long id;
    private BigDecimal amount = BigDecimal.ZERO;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String name;
    private String description;
    private Long operationId;
    private String bankName;

    Income convert() {
        return Income.builder()
                .id(id)
                .amount(amount)
                .date(date)
                .name(name)
                .description(description)
                .build();
    }

    static IncomeDto convert(Income in) {
        return IncomeDto.builder()
                .id(in.getId())
                .amount(in.getAmount())
                .date(in.getDate())
                .name(in.getName())
                .description(in.getDescription())
                .operationId(getOperationIdSave(in.getOperation()))
                .bankName(getBankNameSave(in.getOperation()))
                .build();
    }

    private static String getBankNameSave(Operation operation) {
        return Optional.ofNullable(operation).flatMap(o -> Optional.of(o.getAccount()).map(Account::getBank))
                .orElse(null);
    }

    private static Long getOperationIdSave(Operation operation) {
        return Optional.ofNullable(operation).map(Operation::getId)
                .orElse(null);
    }
}
