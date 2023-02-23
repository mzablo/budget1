package mza.thy.operation;

import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Account;
import mza.thy.domain.Operation;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OperationRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class OperationService {

    private final OperationRepository operationRepository;
    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;
    private final AccountRepository accountRepository;
    private FilterHandler<Operation> filter;

    public OperationService(OperationRepository operationRepository, IncomeRepository incomeRepository, OutcomeRepository outcomeRepository, AccountRepository accountRepository) {
        this.operationRepository = operationRepository;
        this.incomeRepository = incomeRepository;
        this.outcomeRepository = outcomeRepository;
        this.accountRepository = accountRepository;
        this.filter = operationRepository.getFilter();
    }

    @Transactional(readOnly = true)
    public List<OperationDto> getOperationList(FilterParams filterParams, Pageable pageable) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return operationRepository.findAll(pageable)
                .stream()
                .map(OperationDto::convertToDto)
                .collect(Collectors.toList());//
    }

    private List<OperationDto> doFilter(FilterParams filterParams) {
        return filter.getFiltered(filterParams)
                .map(OperationDto::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OperationDto getOperation(Long id) {
        var result = operationRepository.findById(id)
                .map(OperationDto::convertToDto)
                .orElseThrow(() -> new RuntimeException("Operation not found " + id));
        log.debug("Get operation {}", result);
        return result;
    }

    @Transactional
    public void saveOperation(OperationDto operationDto) {
        if (Objects.nonNull(operationDto.getId())) {
            updateOperation(operationDto.getId(), operationDto);
        } else {
            saveNewOperation(operationDto);
        }
    }

    private void saveNewOperation(OperationDto operationDto) {
        if (StringUtils.hasLength(operationDto.getName())) {
            log.debug("Saving new operation " + operationDto);
            operationRepository.save(operationDto.convert(getAccountForBankName(operationDto.getBankName())));
        } else {
            log.debug("Not saved - empty operation {}", operationDto);
        }
    }

    private Account getAccountForBankName(String bankName) {
        return accountRepository.findAllByBankLike(bankName).findFirst().orElse(null);
    }

    @Transactional
    public void deleteOperation(Long id) {
        log.debug("Income to be deleted {}", incomeRepository.findByOperation_Id((id)));
        log.debug("Outcome to be deleted {}", outcomeRepository.findByOperation_Id(id));
        int deletedIncome = incomeRepository.deleteByOperation_Id(id);
        int deletedOutcome = outcomeRepository.deleteByOperation_Id(id);
        operationRepository.deleteById(id);
        log.debug("Deleted operation {}", id);
        log.debug("Number of deleted income {}", deletedIncome);
        log.debug("Number of deleted outcome {}", deletedOutcome);
    }

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
