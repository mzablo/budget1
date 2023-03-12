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
public class MonthlyBalanceDto {
    public static final List<String> MONTHLY_BALANCE = List.of("Year", "|", "Month", "|", "Balance");
    Integer year;
    Integer month;
    BigDecimal balance;

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put(MONTHLY_BALANCE.get(0), year.toString());
        result.put(MONTHLY_BALANCE.get(1), "  |  ");
        result.put(MONTHLY_BALANCE.get(2), month.toString());
        result.put(MONTHLY_BALANCE.get(3), "  |  ");
        result.put(MONTHLY_BALANCE.get(4), decimalFormat.format(balance));
        return result;
    }

}

