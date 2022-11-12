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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IncomeService {

    @Value("${budget.path}")
    private String x;

    private final IncomeRepository incomeRepository;

    @Transactional(readOnly = true)
    List<IncomeDto> getIncomeList(FilterParams filterParams, Sort sort) {
        if (Objects.nonNull(filterParams)) {
            doFilter(filterParams);
        }
        return Optional.ofNullable(sort).map(s->incomeRepository.findAll(sort))
                .orElse(incomeRepository.findAll()).stream()
                .map(IncomeDto::convert)
                .collect(Collectors.toList());
    }

    private List<IncomeDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter income by id " + filterParams.getFilterId());
            return incomeRepository.findById(filterParams.getFilterId())
                    .map(IncomeDto::convert)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter income by name " + filterParams.getFilterName());
            return incomeRepository.findAllByNameLike(filterParams.getFilterName())
                    .stream()
                    .map(IncomeDto::convert)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
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
    void saveIncome(IncomeDto incomeDto) {
        if (Objects.nonNull(incomeDto.getId())) {
            updateIncome(incomeDto.getId(), incomeDto);
        } else {
            saveNewIncome(incomeDto);
        }
    }

    private void saveNewIncome(IncomeDto incomeDto) {
        if (Objects.nonNull(incomeDto.getName())) {
            log.debug("Saving new income " + incomeDto);
            incomeRepository.save(incomeDto.convert());
        } else {
            log.debug("Not saved - empty income " + incomeDto);
        }
    }

    @Transactional
    void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
        log.debug("Deleted income " + id);
    }

    private void updateIncome(Long id, IncomeDto incomeDto) {
        var income = incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brak income " + id));
        income.setAmount(incomeDto.getAmount());
        income.setDate(incomeDto.getDate());
        income.setDescription(incomeDto.getDescription());
        income.setOperationId(incomeDto.getOperationId());//!
        log.debug("Update income " + incomeDto);
        incomeRepository.save(income);
    }

}
