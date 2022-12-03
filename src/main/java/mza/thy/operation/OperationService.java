package mza.thy.operation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Account;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OperationRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OperationService {

    private final OperationRepository operationRepository;
    private final IncomeRepository incomeRepository;
    //outcomerepo
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<OperationDto> getOperationList(FilterParams filterParams, Pageable pageable) {
        if (Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterAmount())
                || Objects.nonNull(filterParams.getFilterName())
                || Objects.nonNull(filterParams.getFilterDescription())
                || Objects.nonNull(filterParams.getFilterDate()))
        ) {
            return doFilter(filterParams);
        }
        return operationRepository.findAll(pageable)//!!!
                .stream()
                .map(OperationDto::convertToDto)
                .collect(Collectors.toList());//!!! zwracac liste czy page?
    }

    private List<OperationDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter operation by id " + filterParams.getFilterId());
            return operationRepository.findById(filterParams.getFilterId())
                    .map(OperationDto::convertToDto)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterDate())) {
            log.debug("Filter operation by date {}", filterParams.getFilterDate());
            return operationRepository.findAllByDate(filterParams.getFilterDate())
                    .map(OperationDto::convertToDto)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterAmount())) {
            log.debug("Filter operation by amount {}", filterParams.getFilterAmount());
            return operationRepository.findAllByAmount(filterParams.getFilterAmount())
                    .map(OperationDto::convertToDto)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter operation by name {}", filterParams.getFilterName());
            return operationRepository.findAllByNameLike(filterParams.getFilterName().getValue())
                    .map(OperationDto::convertToDto)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterDescription())) {
            log.debug("Filter operation by description {}", filterParams.getFilterDescription());
            return operationRepository.findAllByDescriptionLike(filterParams.getFilterDescription().getValue())
                    .map(OperationDto::convertToDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    OperationDto getOperation(Long id) {
        var result = operationRepository.findById(id)
                .map(OperationDto::convertToDto)
                .orElseThrow(() -> new RuntimeException("Operation not found " + id));
        log.debug("Get operation {}", result);
        return result;
    }

    @Transactional
    void saveOperation(OperationDto operationDto) {
        if (Objects.nonNull(operationDto.getId())) {
            updateOperation(operationDto.getId(), operationDto);
        } else {
            saveNewOperation(operationDto);
        }
    }

    private void saveNewOperation(OperationDto operationDto) {
        if (Objects.nonNull(operationDto.getName())) {
            log.debug("Saving new operation " + operationDto);
            operationRepository.save(operationDto.convert(getAccountForBankName(operationDto.getBankName())));
        } else {
            log.debug("Not saved - empty operation {}", operationDto);
        }
    }

    private Account getAccountForBankName(String bankName) {
        return accountRepository.findAllByBankLike(bankName).get(0);
    }

    @Transactional
    void deleteOperation(Long id) {
        operationRepository.deleteById(id);
        int result = incomeRepository.deleteByOperationId(id);//!!! nie dziala?
        log.debug("Deleted operation {}", id);
        log.debug("Number of deleted income {}", result);
        //!!!log.warn("If there is income/outcome for {} - it will not be removed ", id);
    }

    /*  @Transactional(readOnly = true)
      Page<OperationDto> getOperationPage(Pageable pageable) {
          var page = operationRepository.findTotalAll(pageable);
          return page.map(OperationDto::convertToDto);
      }
  */
    private void updateOperation(Long id, OperationDto operationDto) {
        var operation = operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found " + id));
        operation.setName(operationDto.getName());
        operation.setDescription(operationDto.getDescription());
        operation.setAmount(operationDto.getAmount());
        operation.setDate(operationDto.getDate());
        operation.setAccount(getAccountForBankName(operationDto.getBankName()));
        log.debug("Update operation {}", operationDto);
        log.warn("If there is income/outcome for {} - it will not be updated ", id);
        operationRepository.save(operation);
    }
}
