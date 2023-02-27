package mza.thy.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
@ToString
@AllArgsConstructor
public class MonthlyBalanceDto {
    Integer year;
    Integer month;
    BigDecimal balance;
}

