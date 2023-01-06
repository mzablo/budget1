package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;


    @Transactional(readOnly = true)
    public SummaryDto getSummary() {
        var income = incomeRepository.sumIncome();
        var outcome = outcomeRepository.sumOutcome();
        log.debug("Getting summary {} - {}", income, outcome);
        return SummaryDto.builder()
                .totalIncome(income)
                .totalOutcome(outcome)
                .balance(income.subtract(outcome))
                .build();
    }

}
