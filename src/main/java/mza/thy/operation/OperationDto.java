package mza.thy.operation;

import lombok.*;
import mza.thy.domain.Account;
import mza.thy.domain.Operation;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OperationDto {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private BigDecimal amount = BigDecimal.ZERO;
    private String name;
    private String description;
    private String bankName;

    Operation convert(Account account) {
        var operation = new Operation();
        operation.setDate(date);
        operation.setAmount(amount);
        operation.setName(name);
        operation.setAccount(account);
        operation.setDescription(description);
        return operation;
    }

    static OperationDto convertToDto(Operation in) {
        return OperationDto.builder()
                .id(in.getId())
                .date(in.getDate())
                .amount(in.getAmount())
                .bankName(in.getAccount().getBank())
                .name(in.getName())
                .description(in.getDescription())
                .build();
    }
}
