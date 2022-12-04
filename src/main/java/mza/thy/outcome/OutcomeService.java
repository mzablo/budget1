package mza.thy.outcome;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.OperationHandler;
import mza.thy.domain.Outcome;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.OutcomeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutcomeService {
    private final OperationHandler operationHandler;
    private final OutcomeRepository outcomeRepository;

    @Transactional(readOnly = true)
    public List<OutcomeDto> getOutcomeList(FilterParams filterParams, @NotNull Sort sort) {
        if (Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterName())
                || Objects.nonNull(filterParams.getFilterCategory())
                || Objects.nonNull(filterParams.getFilterDate())
                || Objects.nonNull(filterParams.getFilterAmount())
                || Objects.nonNull(filterParams.getFilterDescription()))
        ) {
            return doFilter(filterParams);
        }
        return outcomeRepository.findTotalAll(sort)
                .map(OutcomeDto::convert)
                .collect(Collectors.toList());
    }

    private List<OutcomeDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter outcome by id {}", filterParams.getFilterId());
            return outcomeRepository.findById(filterParams.getFilterId())
                    .map(OutcomeDto::convert)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter outcome by name {}", filterParams.getFilterName());
            return outcomeRepository.findAllByNameLike(filterParams.getFilterName().getValue())
                    .map(OutcomeDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterDescription())) {
            log.debug("Filter outcome by description {}", filterParams.getFilterDescription());
            return outcomeRepository.findAllByDescriptionLike(filterParams.getFilterDescription().getValue())
                    .map(OutcomeDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterCategory())) {
            log.debug("Filter outcome by category {}", filterParams.getFilterCategory());
            return outcomeRepository.findAllByCategoryLike(filterParams.getFilterCategory().getValue())
                    .map(OutcomeDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterDate())) {
            log.debug("Filter outcome by date {}", filterParams.getFilterDate());
            return outcomeRepository.findAllByDate(filterParams.getFilterDate())
                    .map(OutcomeDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterAmount())) {
            log.debug("Filter outcome by amount {}", filterParams.getFilterAmount());
            return outcomeRepository.findAllByPrice(filterParams.getFilterAmount())
                    .map(OutcomeDto::convert)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    Page<OutcomeDto> getOutcomePage(Pageable pageable) {
        var page = outcomeRepository.findAll(pageable);
        return page.map(OutcomeDto::convert);
    }

    @Transactional(readOnly = true)
    OutcomeDto getOutcomeDto(Long id) {
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
    void saveOutcome(OutcomeDto outcomeDto) {
        Outcome outcome;
        if (Objects.nonNull(outcomeDto.getId())) {
            outcome = updateOutcome(outcomeDto.getId(), outcomeDto);
        } else {
            outcome = saveNewOutcome(outcomeDto);
        }
        operationHandler.handleOperation(outcomeDto.getOperationId(), outcomeDto.getBankName(), outcome);
    }

    private Outcome saveNewOutcome(OutcomeDto outcomeDto) {
        if (Objects.nonNull(outcomeDto.getName())) {
            log.debug("Saving new outcome {}", outcomeDto);
            return outcomeRepository.save(outcomeDto.convert());
        } else {
            log.debug("Not saved - empty outcome {} ", outcomeDto);
            return null;
        }
    }

    @Transactional
    void deleteOutcome(Long id) {
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
        outcome.setPrice(changedOutcomeDto.getPrice());
        outcome.setDate(changedOutcomeDto.getDate());
        outcome.setName(changedOutcomeDto.getName());
        outcome.setCategory(changedOutcomeDto.getCategory());
        outcome.setCounter(changedOutcomeDto.getCounter());
        outcome.setDescription(changedOutcomeDto.getDescription());
        log.debug("Update outcome {}", changedOutcomeDto);
        return outcomeRepository.save(outcome);
    }

}