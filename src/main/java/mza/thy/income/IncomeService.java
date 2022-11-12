package mza.thy.income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeService {

    @Value("${budget.path}")
    private String x;

    private final IncomeRepository incomeRepository;

    @Transactional(readOnly = true)
    List<IncomeDto> getIncomeList() {
        return incomeRepository.findAll(Sort.by(Sort.Direction.DESC, "date")).stream()
                .map(IncomeDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    Page<IncomeDto> getIncomePage(Pageable pageable) {
        var page = incomeRepository.findAll(pageable);
        return page.map(IncomeDto::convert);
    }

    @Transactional(readOnly = true)
    IncomeDto getIncome(Long id) {
        return incomeRepository.findById(id)
                .map(IncomeDto::convert)
                .orElseThrow(() -> new RuntimeException("Income not found " + id));
    }

    @Transactional
    IncomeDto saveNewIncome(IncomeDto incomeDto) {
        log.debug("Saving new income " + incomeDto);
        var income = incomeDto.convert();
        return IncomeDto.convert(incomeRepository.save(income));
    }

    @Transactional
    IncomeDto updateIncome(Long id, IncomeDto incomeDto) {
        var income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brak income " + id));
        income.setAmount(incomeDto.getAmount());
        income.setDate(incomeDto.getDate());
        income.setOperationId(incomeDto.getOperationId());
        income.setDescription(incomeDto.getDescription());

        return IncomeDto.convert(incomeRepository.save(income));
    }

}
