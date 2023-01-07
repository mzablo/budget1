package mza.thy.summary;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
@ToString
public class SummaryDto {
    String totalIncome;
    String totalOutcome;
    String balance;
    Integer year;
    Integer month;
}

