package mza.thy.account;

import lombok.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DepositDto {
    private String bank;
    private BigDecimal balance;

    public Map<String, String> getMap(DecimalFormat decimalFormat) {
        Map<String, String> result = new HashMap<>();
        result.put("BANK", bank);
        result.put("|", "  |  ");
        result.put("BALANCE", decimalFormat.format(balance));
        return result;
    }

}
