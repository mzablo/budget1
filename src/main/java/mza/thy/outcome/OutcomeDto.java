package mza.thy.outcome;

import lombok.*;
import mza.thy.domain.Account;
import mza.thy.domain.Operation;
import mza.thy.domain.Outcome;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class OutcomeDto {
    private Long id;
    private BigDecimal price = BigDecimal.ZERO;
    private BigDecimal add = BigDecimal.ZERO;
    private Integer counter = 1;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date = LocalDate.now();
    private String name;
    private String description;
    private String category;
    private String categoryFreeText;
    private Long operationId;
    private String bankName;

    Outcome convert() {
        var outcome = new Outcome();
        outcome.setPrice(price.add(add));
        outcome.setCounter(counter);
        outcome.setDate(date);
        outcome.setName(name);
        outcome.setDescription(description);
        outcome.setCategory(getAnyCategory());
        return outcome;
    }

    String getAnyCategory() {
        return StringUtils.hasLength(categoryFreeText) ? categoryFreeText : category;
    }

    static OutcomeDto convert(Outcome in) {
        return OutcomeDto.builder()
                .id(in.getId())
                .price(in.getPrice())
                .counter(in.getCounter())
                .date(in.getDate())
                .name(in.getName())
                .description(in.getDescription())
                .category(in.getCategory())
                .categoryFreeText(in.getCategory())
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
