package mza.thy.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@Value
@ToString
@AllArgsConstructor
public class YearlyBalanceDto {
    public static final List<String> YEARLY_BALANCE = List.of(
            "Year", "|",
            "Income", "|",
            "Outcome", "|",
            "Balance", "|",
            "Total balance"
    );
    Integer year;
    BigDecimal income;
    BigDecimal outcome;
    BigDecimal balance;
    BigDecimal totalBalance = BigDecimal.ZERO;

    public YearlyBalanceDto(Integer year, BigDecimal income, BigDecimal outcome) {
        this.year = year;
        this.income = income;
        this.outcome = outcome;
        this.balance = BigDecimal.ZERO;
    }

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put(YEARLY_BALANCE.get(0), year.toString());
        result.put(YEARLY_BALANCE.get(1), "  |  ");
        result.put(YEARLY_BALANCE.get(2), decimalFormat.format(income));
        result.put(YEARLY_BALANCE.get(3), "  |  ");
        result.put(YEARLY_BALANCE.get(4), decimalFormat.format(outcome));
        result.put(YEARLY_BALANCE.get(5), "  |  ");
        result.put(YEARLY_BALANCE.get(6), decimalFormat.format(balance));
        result.put(YEARLY_BALANCE.get(7), "  |  ");
        result.put(YEARLY_BALANCE.get(8), decimalFormat.format(totalBalance));
        return result;
    }

    //year month income, outcome, balance, total balance
}

