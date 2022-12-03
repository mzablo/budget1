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
    private LocalDate date;
    private BigDecimal amount;
    private String name;
    private String description;
    private String bankName;

    Operation convert(Account account) {
        return Operation.builder()
                .id(id)
                .date(date)
                .amount(amount)
                .name(name)
                .account(account)
                .description(description)
                .build();
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
