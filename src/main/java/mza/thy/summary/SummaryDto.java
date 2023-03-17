package mza.thy.summary;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
@ToString
public class SummaryDto {
    String totalDeposit;
    String totalAccounts;
    String totalAdb;
    String pocket;
    String totalIncome;
    String totalOutcome;
    String realBalance;
    String balance;
    Integer year;
    Integer month;

    List<String> headers;
    List<Map<String, String>> rows;

    List<String> monthlyHeaders;
    List<Map<String, String>> monthlyRows;
}

