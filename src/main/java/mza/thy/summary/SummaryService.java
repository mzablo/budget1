package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.MonthlyBalanceDto;
import mza.thy.common.YearlyBalanceDto;
import mza.thy.repository.DepositRepository;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OperationRepository;
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
    private final DepositRepository depositRepository;
    private final OperationRepository operationRepository;
    private final DecimalFormat decimalFormat;
    private String error;

    @Transactional(readOnly = true)
    public SummaryDto getSummary() {

        var income = Optional.ofNullable(incomeRepository.sumIncome()).orElse(BigDecimal.ZERO);
        var outcome = Optional.ofNullable(outcomeRepository.sumOutcome()).orElse(BigDecimal.ZERO);
        var balance = income.subtract(outcome);
        var sumActiveDeposit = depositRepository.sumActiveDeposit();
        var sumAdbActiveDeposit = depositRepository.sumAdbActiveDeposit();
        var sumOperations = operationRepository.sumOperations();
        var sumAdbOperations = operationRepository.sumAdbOperations();
        var pocket = balance.subtract(sumActiveDeposit).subtract(sumOperations);

        log.debug("Getting summary {} - {}", income, outcome);
        return SummaryDto.builder()
                .totalDeposit(decimalFormat.format(sumActiveDeposit))
                .totalAccounts(decimalFormat.format(sumOperations))
                .totalAdb(decimalFormat.format(sumAdbActiveDeposit.add(sumAdbOperations)))
                .pocket(decimalFormat.format(pocket))
                .totalIncome(decimalFormat.format(income))
                .totalOutcome(decimalFormat.format(outcome))
                .balance(decimalFormat.format(balance))
                .realBalance(decimalFormat.format(sumActiveDeposit.add(sumOperations)))
                .headers(YearlyBalanceDto.YEARLY_BALANCE)
                .rows(buildRows())

                .monthlyHeaders(MonthlyBalanceDto.MONTHLY_BALANCE)
                .monthlyRows(buildMonthlyRows())
                .build();
    }

    private List<Map<String, String>> buildRows() {
        return incomeRepository.yearlyBalance().stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }

    private List<Map<String, String>> buildMonthlyRows() {
        return incomeRepository.monthlyBalance().stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }

    //year  income, outcome, balance, total balance
    //year month income, outcome, balance, total balance
    String getError() {
        return error;
    }
}
