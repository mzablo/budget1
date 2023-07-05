package mza.thy.chart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.OutcomeByCategoryHelperDto;
import mza.thy.common.YearlyBalanceHelperDto;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
class ChartService {
    private final Clock clock;
    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;

    Map<Integer, BigDecimal> getYearlyBalance() {
        var yearlyIncomeBalance = incomeRepository.yearlyBalance();
        var yearlyOutcomeBalance = outcomeRepository.yearlyBalance().stream()
                .collect(Collectors.toMap(YearlyBalanceHelperDto::getYear, Function.identity()));
        return yearlyIncomeBalance.stream()
                .collect(Collectors.toMap(YearlyBalanceHelperDto::getYear, (y) -> getBalance(y, yearlyOutcomeBalance)));
    }

    private BigDecimal getBalance(YearlyBalanceHelperDto yearlyIncomeBalance, Map<Integer, YearlyBalanceHelperDto> yearlyOutcomeBalance) {
        var outcome = BigDecimal.ZERO;
        try {
            outcome = yearlyOutcomeBalance.get(yearlyIncomeBalance.getYear()).getAmount();
        } catch (Exception e) {
            log.error("Outcome balance for {} not available error: {}", yearlyIncomeBalance.getYear(), e.getMessage());
        }
        return yearlyIncomeBalance.getAmount().subtract(outcome);
    }


    Map<Integer, BigDecimal> getYearlyIncome() {
        return incomeRepository.yearlyBalance().stream()
                .collect(Collectors.toMap(YearlyBalanceHelperDto::getYear, YearlyBalanceHelperDto::getAmount));
    }

    @Transactional(readOnly = true)
    public Map<String, BigDecimal> getLastOutcomePerCategory(Integer year, Integer month) {
        return getOutcomePerCategoryData(year == null ? Year.now(clock).getValue() : year,
                month == null ? Calendar.getInstance().get(Calendar.MONTH) : month);
    }

    private Map<String, BigDecimal> getOutcomePerCategoryData(Integer year, Integer month) {
        log.debug("Getting outcome by cat from year {} and month {}", year, month);
        return outcomeRepository.outcomeByCategory(year, month)
                .collect(Collectors.toMap(OutcomeByCategoryHelperDto::getCategory, OutcomeByCategoryHelperDto::getAmount));
    }

}
