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
    public static final List<String> YEARLY_BALANCE = List.of("Year", "|", "balance");
    Integer year;
    BigDecimal balance;

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put(YEARLY_BALANCE.get(0), year.toString());
        result.put(YEARLY_BALANCE.get(1), "  |  ");
        result.put(YEARLY_BALANCE.get(2), decimalFormat.format(balance));
        return result;
    }
}

