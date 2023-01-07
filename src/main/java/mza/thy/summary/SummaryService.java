package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

@Service
@Slf4j
@RequiredArgsConstructor
public class SummaryService {

    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;


    @Transactional(readOnly = true)
    public SummaryDto getSummary() {
        DecimalFormat df = new DecimalFormat("###,##0.00");

        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(customSymbol);

        var income = incomeRepository.sumIncome();
        var outcome = outcomeRepository.sumOutcome();
        var balance = income.subtract(outcome);

        log.debug("Getting summary {} - {}", income, outcome);
        return SummaryDto.builder()
                .totalIncome(df.format(income))
                .totalOutcome(df.format(outcome))
                .balance(df.format(balance))
                .build();
    }

}
