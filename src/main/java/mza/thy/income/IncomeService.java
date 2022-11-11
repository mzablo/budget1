package mza.thy.income;

import lombok.RequiredArgsConstructor;
import mza.thy.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final IncomeRepository incomeRepository;

    @Transactional(readOnly = true)
    IncomeListDto getIncomeList() {
        return new IncomeListDto(incomeRepository.findAll().stream()
                .map(IncomeDto::convert)
                .collect(Collectors.toList()));
    }

    @Transactional
    IncomeDto saveNewIncome(IncomeDto incomeDto) {
        var income = incomeDto.convert();
        return IncomeDto.convert(incomeRepository.save(income));
    }

    @Transactional
    IncomeDto updateIncome(IncomeDto incomeDto) {
        var income = incomeRepository.findById(incomeDto.getId())
                .orElseThrow(() -> new RuntimeException("Brak income " + incomeDto.getId()));
        income.setAmount(incomeDto.getAmount());
        income.setDate(incomeDto.getDate());
        income.setOperationId(incomeDto.getOperationId());
        income.setDescription(incomeDto.getDescription());

        return IncomeDto.convert(incomeRepository.save(income));
    }

}
