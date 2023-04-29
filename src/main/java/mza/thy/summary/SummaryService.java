package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.MonthlyBalanceDto;
import mza.thy.common.MonthlyBalanceHelperDto;
import mza.thy.common.YearlyBalanceDto;
import mza.thy.common.YearlyBalanceHelperDto;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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

        var income = getIncome();
        var outcome = getOutcome();
        var balance = income.subtract(outcome);
        var sumActiveDeposit = getSumActiveDeposit();
        var sumAdbActiveDeposit = getSumAdbActiveDeposit();
        var sumOperations = getSumOperations();
        var sumAdbOperations = getSumAdbOperations();
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


    private BigDecimal getSumAdbOperations() {
        return operationRepository.sumAdbOperations();
    }

    private BigDecimal getSumOperations() {
        return operationRepository.sumOperations();
    }

    private BigDecimal getSumAdbActiveDeposit() {
        return depositRepository.sumAdbActiveDeposit();
    }

    private BigDecimal getSumActiveDeposit() {
        return depositRepository.sumActiveDeposit();
    }

    private BigDecimal getOutcome() {
        return Optional.ofNullable(outcomeRepository.sumOutcome()).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getIncome() {
        return Optional.ofNullable(incomeRepository.sumIncome()).orElse(BigDecimal.ZERO);
    }

    private List<Map<String, String>> buildRows() {
        var yearlyIncomeBalance = incomeRepository.yearlyBalance();
        var yearlyOutcomeBalance = outcomeRepository.yearlyBalance()
                .stream().collect(Collectors.toMap(YearlyBalanceHelperDto::getYear, Function.identity()));
        var balance = yearlyIncomeBalance.stream()
                .map(i -> buildYearlyBalance(i, yearlyOutcomeBalance.get(i.getYear())))
                .collect(Collectors.toList());
        BigDecimal helpBalance = BigDecimal.ZERO;
        for (int i = balance.size() - 1; i >= 0; i--) {
            helpBalance = balance.get(i).getBalance().add(helpBalance);
            balance.get(i).setTotalBalance(helpBalance);
        }

        return balance
                .stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }

    private YearlyBalanceDto buildYearlyBalance(YearlyBalanceHelperDto income, YearlyBalanceHelperDto outcome) {
        if (Objects.isNull(outcome)) {
            return new YearlyBalanceDto(income.getYear(), income.getAmount(), null);
        }
        return new YearlyBalanceDto(
                income.getYear(),
                income.getAmount(),
                outcome.getAmount());
    }

    private MonthlyBalanceDto buildMonthlyBalance(MonthlyBalanceHelperDto income, MonthlyBalanceHelperDto outcome) {
        if (Objects.isNull(outcome)) {
            return new MonthlyBalanceDto(income.getYear(), income.getMonth(), income.getAmount(), null);
        }
        return new MonthlyBalanceDto(
                income.getYear(),
                income.getMonth(),
                income.getAmount(),
                outcome.getAmount());
    }

    private List<Map<String, String>> buildMonthlyRows() {
        var monthlyIncomeBalance = incomeRepository.monthlyBalance();
        var monthlyOutcomeBalance = outcomeRepository.monthlyBalance()
                .stream().collect(Collectors.toMap(m -> m.getYear() + "." + m.getMonth(), Function.identity()));
        var balance = monthlyIncomeBalance.stream()
                .map(i -> buildMonthlyBalance(i, monthlyOutcomeBalance.get(i.getYear() + "." + i.getMonth())))
                .collect(Collectors.toList());
        BigDecimal helpBalance = BigDecimal.ZERO;
        for (int i = balance.size() - 1; i >= 0; i--) {
            if (Objects.nonNull(balance.get(i).getBalance())) {
                helpBalance = balance.get(i).getBalance().add(helpBalance);
            }
            balance.get(i).setTotalBalance(helpBalance);
        }

        return balance
                .stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }

    String getError() {
        return error;
    }
}
