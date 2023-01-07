package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;
    private final DecimalFormat decimalFormat;

    @Transactional(readOnly = true)
    public SummaryDto getSummary() {

        var income = incomeRepository.sumIncome();
        var outcome = outcomeRepository.sumOutcome();
        var balance = income.subtract(outcome);

        log.debug("Getting summary {} - {}", income, outcome);
        return SummaryDto.builder()
                .totalIncome(decimalFormat.format(income))
                .totalOutcome(decimalFormat.format(outcome))
                .balance(decimalFormat.format(balance))
                .build();
    }

}
