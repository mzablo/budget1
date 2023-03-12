package mza.thy.common;

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
//@AllArgsConstructor
public class YearlyBalanceDto {
    public static final List<String> YEARLY_BALANCE = List.of("Year", "|",
            "Income", "|",
            "Outcome", "|",
            "Balance", "|",
            "Total balance"
    );
    Integer year;
    BigDecimal income;
    BigDecimal outcome;
    BigDecimal balance = BigDecimal.ZERO;
    BigDecimal totalBalance = BigDecimal.ZERO;

    public YearlyBalanceDto(Integer year, BigDecimal income, BigDecimal outcome) {
        this.year = year;
        this.income = income;
        this.outcome = outcome;
    }
    public YearlyBalanceDto(Integer year, BigDecimal income) {
        this.year = year;
        this.income = income;
        this.outcome = BigDecimal.ZERO;
    }
   /* public YearlyBalanceDto(Integer year, String income, String outcome) {
        this.year = year;
        this.income = BigDecimal.ZERO;//income;
        this.outcome = BigDecimal.ZERO;//outcome;
    }*/

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

    //year  income, outcome, balance, total balance
    //year month income, outcome, balance, total balance

}

