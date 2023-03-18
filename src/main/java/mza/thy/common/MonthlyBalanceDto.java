package mza.thy.common;

import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@Data
@ToString
@AllArgsConstructor
public class MonthlyBalanceDto {
    public static final List<String> MONTHLY_BALANCE = List.of(
            "Year", "|",
            "Month", "|",
            "Income", "|",
            "Outcome", "|",
            "Balance", "|",
            "Total balance"
    );
    private Integer year;
    private Integer month;
    private BigDecimal income;
    private BigDecimal outcome;
    private BigDecimal balance;
    private BigDecimal totalBalance = BigDecimal.ZERO;

    public MonthlyBalanceDto(Integer year, Integer month, BigDecimal income, BigDecimal outcome) {
        this.year = year;
        this.month = month;
        this.income = income;
        this.outcome = outcome;
        if (Objects.nonNull(income) && Objects.nonNull(outcome)) {
            this.balance = income.subtract(outcome);
        }
    }

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put(MONTHLY_BALANCE.get(0), year.toString());
        result.put(MONTHLY_BALANCE.get(1), "  |  ");
        result.put(MONTHLY_BALANCE.get(2), month.toString());
        result.put(MONTHLY_BALANCE.get(3), "  |  ");
        result.put(MONTHLY_BALANCE.get(4), decimalFormat.format(income));
        result.put(MONTHLY_BALANCE.get(5), "  |  ");
        result.put(MONTHLY_BALANCE.get(6), decimalFormat.format(outcome));
        result.put(MONTHLY_BALANCE.get(7), "  |  ");
        result.put(MONTHLY_BALANCE.get(8), decimalFormat.format(balance));
        result.put(MONTHLY_BALANCE.get(9), "  |  ");
        result.put(MONTHLY_BALANCE.get(10), decimalFormat.format(totalBalance));
        return result;
    }
}

