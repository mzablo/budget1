package mza.thy.income;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Income;
import mza.thy.domain.OperationHandler;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class IncomeService {
    @Value("${budget.path}")
    private String x;
    private final OperationHandler operationHandler;
    private final IncomeRepository incomeRepository;
    private FilterHandler<Income> filterHandler;

    public IncomeService(OperationHandler operationHandler, IncomeRepository incomeRepository) {
        this.operationHandler = operationHandler;
        this.incomeRepository = incomeRepository;
        filterHandler = incomeRepository.getFilter();
    }

    @Transactional(readOnly = true)
    public List<IncomeDto> getIncomeList(FilterParams filterParams, @NotNull Sort sort) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return incomeRepository.findTotalAll(sort)
                .map(IncomeDto::convert)
                .collect(Collectors.toList());
    }

    private List<IncomeDto> doFilter(FilterParams filterParams) {
        return filterHandler.getFiltered(filterParams)
                .map(IncomeDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    Page<IncomeDto> getIncomePage(Pageable pageable) {
        var page = incomeRepository.findAll(pageable);
        return page.map(IncomeDto::convert);
    }

    @Transactional(readOnly = true)
    IncomeDto getIncomeDto(Long id) {
        var result = incomeRepository.findById(id)
                .map(IncomeDto::convert)
                .orElseThrow(() -> new RuntimeException("Income not found " + id));
        log.debug("Get income {}", result);
        if (Objects.nonNull(result.getOperationId())) {
            log.debug("Get operation {}", operationHandler.getOperation(result.getOperationId()));
        }
        return result;
    }


    @Transactional
    long saveIncome(IncomeDto incomeDto) {
        Income income;
        if (Objects.nonNull(incomeDto.getId())) {
            income = updateIncome(incomeDto.getId(), incomeDto);
        } else {
            income = saveNewIncome(incomeDto);
        }
        operationHandler.handleOperation(incomeDto.getOperationId(), incomeDto.getBankName(), income);
        return income.getId();
    }

    private Income saveNewIncome(IncomeDto incomeDto) {
        if (StringUtils.hasLength(incomeDto.getName())) {
            log.debug("Saving new income {}", incomeDto);
            return incomeRepository.save(incomeDto.convert());
        } else {
            log.debug("Not saved - empty income {} ", incomeDto);
            return null;
        }
    }

    @Transactional
    public void deleteIncome(Long id) {
        operationHandler.deleteOperation(getIncomeDto(id).getOperationId());
        incomeRepository.deleteById(id);
        log.debug("Deleted income {}", id);
    }

    Income getIncome(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found " + id));
    }

    private Income updateIncome(Long id, IncomeDto changedIncomeDto) {
        var income = getIncome(id);
        income.setAmount(changedIncomeDto.getAmount());
        income.setDate(changedIncomeDto.getDate());
        income.setName(changedIncomeDto.getName());
        income.setDescription(changedIncomeDto.getDescription());
        log.debug("Update income {}", changedIncomeDto);
        return incomeRepository.save(income);
    }

}
