package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;
    private final DecimalFormat decimalFormat;

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
                .build();
    }

}
