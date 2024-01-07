package mza.thy.common;

import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Builder
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YearlyBalanceDto {
    public static final List<String> YEARLY_BALANCE = List.of(
            "Year", "|",
            "Income", "|",
            "Outcome", "|",
            "Balance", "|",
            "Total balance"
    );
    private Integer year;
    private BigDecimal income;
    private BigDecimal outcome;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal totalBalance = BigDecimal.ZERO;


    public YearlyBalanceDto(Integer year, BigDecimal income, BigDecimal outcome) {
        this.year = year;
        this.income = income;
        this.outcome = outcome;
        if (Objects.nonNull(income) && Objects.nonNull(outcome)) {
            this.balance = income.subtract(outcome);
        }
    }

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put(YEARLY_BALANCE.get(0), year.toString());
        result.put(YEARLY_BALANCE.get(1), "  |  ");
        result.put(YEARLY_BALANCE.get(2), getFormattedValue(decimalFormat, income));
        result.put(YEARLY_BALANCE.get(3), "  |  ");
        result.put(YEARLY_BALANCE.get(4), getFormattedValue(decimalFormat, outcome));
        result.put(YEARLY_BALANCE.get(5), "  |  ");
        result.put(YEARLY_BALANCE.get(6), getFormattedValue(decimalFormat, balance));
        result.put(YEARLY_BALANCE.get(7), "  |  ");
        result.put(YEARLY_BALANCE.get(8), getFormattedValue(decimalFormat, totalBalance));
        return result;
    }

    private String getFormattedValue(DecimalFormat decimalFormat, BigDecimal value) {
        return Optional.ofNullable(value).map(decimalFormat::format).orElse(null);
    }
}

