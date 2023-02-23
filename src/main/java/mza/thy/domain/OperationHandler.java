package mza.thy.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OperationRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperationHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final IncomeRepository incomeRepository;
    private final OutcomeRepository outcomeRepository;

    @Transactional
    public void handleOperation(Long operationId, String bankName, Income income) {
        if (Objects.isNull(operationId) && !StringUtils.hasLength(bankName)) {
            log.debug("No change in operation");
            return;
        }
        Operation operation = Optional.ofNullable(operationId)
                .map(this::getOperation)
                .orElse(null);

        if (isNewOperationToBeCreated(operation, bankName)) {
            createNewOperation(bankName, income);
        } else if (isOperationToBeDeleted(operation, bankName)) {
            deleteOperation(income, operation);
        } else {
            updateOperation(operation, bankName, income);
        }
    }

    public void handleOperation(Long operationId, String bankName, Outcome outcome) {
        if (Objects.isNull(operationId) && !StringUtils.hasLength(bankName)) {
            log.debug("No change in operation");
            return;
        }
        Operation operation = Optional.ofNullable(operationId)
                .map(this::getOperation)
                .orElse(null);

        if (isNewOperationToBeCreated(operation, bankName)) {
            createNewOperation(bankName, outcome);
        } else if (isOperationToBeDeleted(operation, bankName)) {
            deleteOperation(outcome, operation);
        } else {
            updateOperation(operation, bankName, outcome);
        }
    }

    private void deleteOperation(Income income, Operation operation) {
        log.debug("Delete operation {}", operation);
        operationRepository.delete(operation);
        income.setOperation(null);
        incomeRepository.save(income);
    }

    private void deleteOperation(Outcome outcome, Operation operation) {
        log.debug("Delete operation {}", operation);
        operationRepository.delete(operation);
        outcome.setOperation(null);
        outcomeRepository.save(outcome);
    }

    public void deleteOperation(Long id) {
        if (Objects.nonNull(id)) {
            operationRepository.deleteById(id);
            log.debug("Operation deleted {}", id);
        }
    }

    private void updateOperation(Operation oldOperation, String bankName, Income income) {
        oldOperation.setAccount(getAccount(bankName));
        oldOperation.setDate(income.getDate());
        oldOperation.setName(income.getName());
        oldOperation.setDescription(income.getDescription());
        oldOperation.setAmount(income.getAmount());
        operationRepository.save(oldOperation);
        log.debug("Operation {} saved", oldOperation);
    }

    private void updateOperation(Operation oldOperation, String bankName, Outcome outcome) {
        oldOperation.setAccount(getAccount(bankName));
        oldOperation.setDate(outcome.getDate());
        oldOperation.setName(outcome.getName());
        oldOperation.setDescription(outcome.getDescription());
        oldOperation.setAmount(BigDecimal.ZERO.subtract(outcome.getPrice()).multiply(BigDecimal.valueOf(outcome.getCounter())));//!!!check
        operationRepository.save(oldOperation);
        log.debug("Operation {} saved", oldOperation);
    }

    private void createNewOperation(String newAccountName, Income income) {
        Operation newOperation;
        newOperation = Operation.builder()
                .amount(income.getAmount())
                .name(income.getName())
                .description(income.getDescription())
                .date(income.getDate())
                .account(getAccount(newAccountName))
                .build();
        newOperation = operationRepository.save(newOperation);
        income.setOperation(newOperation);
        incomeRepository.save(income);
        log.debug("Saved new operation {} and attached to income {}", newOperation, income.getId());
    }

    private void createNewOperation(String newAccountName, Outcome outcome) {
        Operation newOperation;
        newOperation = Operation.builder()
                .amount(BigDecimal.ZERO.subtract(outcome.getPrice()).multiply(BigDecimal.valueOf(outcome.getCounter())))
                .name(outcome.getName())
                .description(outcome.getDescription())
                .date(outcome.getDate())
                .account(getAccount(newAccountName))
                .build();
        newOperation = operationRepository.save(newOperation);
        outcome.setOperation(newOperation);
        outcomeRepository.save(outcome);
        log.debug("Saved new operation {} and attached to outcome {}", newOperation, outcome.getId());
    }

    private boolean isNewOperationToBeCreated(Operation operation, String bankName) {
        return Objects.isNull(operation) && StringUtils.hasLength(bankName);
    }

    private boolean isOperationToBeDeleted(Operation oldOperation, String newBankName) {
        return Objects.nonNull(oldOperation) && !StringUtils.hasLength(newBankName);
    }

    public Operation getOperation(Long id) {
        return operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found " + id));
    }

    private Account getAccount(String bankName) {
        return accountRepository.findAllByBankLike(bankName)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account not found " + bankName));
    }
}
