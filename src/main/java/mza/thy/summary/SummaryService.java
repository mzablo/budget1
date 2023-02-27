package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.YearlyBalanceDto;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;
    private final DecimalFormat decimalFormat;
    private String error;

    @Transactional(readOnly = true)
    public SummaryDto getSummary() {

        var income = Optional.ofNullable(incomeRepository.sumIncome()).orElse(BigDecimal.ZERO);
        var outcome = Optional.ofNullable(outcomeRepository.sumOutcome()).orElse(BigDecimal.ZERO);
        var balance = income.subtract(outcome);

        log.debug("Getting summary {} - {}", income, outcome);
        return SummaryDto.builder()
                .totalIncome(decimalFormat.format(income))
                .totalOutcome(decimalFormat.format(outcome))
                .balance(decimalFormat.format(balance))
                .headers(YearlyBalanceDto.YEARLY_BALANCE)
                .rows(buildRows())
                .build();
    }

    private List<Map<String, String>> buildRows() {
        return incomeRepository.yearlyBalanceIncome().stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }

    //year  income, outcome, balance, total balance
    //year month income, outcome, balance, total balance
    String getError() {
        return error;
    }
}
