package mza.thy.outcome;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.OperationHandler;
import mza.thy.domain.Outcome;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.OutcomeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
class OutcomeService {

    private final OperationHandler operationHandler;
    private final OutcomeRepository outcomeRepository;
    private final List<String> categoryList;
    private final FilterHandler<Outcome> filter;

    public OutcomeService(OperationHandler operationHandler, OutcomeRepository outcomeRepository) {
        this.operationHandler = operationHandler;
        this.outcomeRepository = outcomeRepository;
        this.filter = outcomeRepository.getFilter();
        this.categoryList = prepareCategoryList();
    }

    List<String> getCategoryList() {
        return categoryList;
    }

    private List<String> prepareCategoryList() {
        try (InputStream input = new FileInputStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            String categories = prop.getProperty("outcomeCat", "brak");
            return Arrays.asList(categories.split(";"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of("empty");
    }

    @Transactional(readOnly = true)
    public long getTotal() {
        return outcomeRepository.count();
    }

    @Transactional(readOnly = true)
    public List<OutcomeDto> getOutcomeList(FilterParams filterParams, @NotNull Pageable page) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return outcomeRepository.findTotalAll(page)
                .map(OutcomeDto::convert)
                .collect(Collectors.toList());
    }

    private List<OutcomeDto> doFilter(FilterParams filterParams) {
        return filter.getFiltered(filterParams)
                .map(OutcomeDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<OutcomeDto> getOutcomePage(Pageable pageable) {
        var page = outcomeRepository.findAll(pageable);
        return page.map(OutcomeDto::convert);
    }

    @Transactional(readOnly = true)
    public OutcomeDto getOutcomeDto(Long id) {
        var result = outcomeRepository.findById(id)
                .map(OutcomeDto::convert)
                .orElseThrow(() -> new RuntimeException("Outcome not found " + id));
        log.debug("Get outcome {}", result);
        if (Objects.nonNull(result.getOperationId())) {
            log.debug("Get operation {}", operationHandler.getOperation(result.getOperationId()));
        }
        return result;
    }


    @Transactional
    public long saveOutcome(OutcomeDto outcomeDto) {
        Outcome outcome;
        if (Objects.nonNull(outcomeDto.getId())) {
            outcome = updateOutcome(outcomeDto.getId(), outcomeDto);
        } else {
            outcome = saveNewOutcome(outcomeDto);
        }
        operationHandler.handleOperation(outcomeDto.getOperationId(), outcomeDto.getBankName(), outcome);
        return outcome.getId();
    }

    private Outcome saveNewOutcome(OutcomeDto outcomeDto) {
        log.debug("Saving new outcome {}", outcomeDto);
        return outcomeRepository.save(outcomeDto.convert());
    }

    @Transactional
    public void deleteOutcome(Long id) {
        operationHandler.deleteOperation(getOutcomeDto(id).getOperationId());
        outcomeRepository.deleteById(id);
        log.debug("Deleted outcome {}", id);
    }

    Outcome getOutcome(Long id) {
        return outcomeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Outcome not found " + id));
    }

    private Outcome updateOutcome(Long id, OutcomeDto changedOutcomeDto) {
        var outcome = getOutcome(id);
        outcome.setPrice(getPriceSave(changedOutcomeDto).add(getAddSafe(changedOutcomeDto.getAdd())));
        outcome.setDate(changedOutcomeDto.getDate());
        outcome.setName(changedOutcomeDto.getName());
        outcome.setCategory(changedOutcomeDto.getAnyCategory());
        outcome.setCounter(changedOutcomeDto.getCounter());
        outcome.setDescription(changedOutcomeDto.getDescription());
        log.debug("Update outcome {}", changedOutcomeDto);
        return outcomeRepository.save(outcome);
    }

    private BigDecimal getPriceSave(OutcomeDto changedOutcomeDto) {
        return Optional.ofNullable(changedOutcomeDto.getPrice()).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getAddSafe(BigDecimal add) {
        return Optional.ofNullable(add).orElse(BigDecimal.ZERO);
    }
}
