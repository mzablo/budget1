package mza.thy.income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Account;
import mza.thy.domain.Income;
import mza.thy.domain.Operation;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OperationHandler {
    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;
    private final IncomeRepository incomeRepository;

    void handleOperation(Long operationId, String bankName, Income income) {
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

    private void deleteOperation(Income income, Operation operation) {
        log.debug("Delete operation {}", operation);
        operationRepository.delete(operation);
        income.setOperation(null);
        incomeRepository.save(income);
    }

    void deleteOperation(Long id) {
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

    private boolean isNewOperationToBeCreated(Operation operation, String bankName) {
        return Objects.isNull(operation) && StringUtils.hasLength(bankName);
    }

    private boolean isOperationToBeDeleted(Operation oldOperation, String newBankName) {
        return Objects.nonNull(oldOperation) && !StringUtils.hasLength(newBankName);
    }

    public Operation getOperation(Long id) {//!!
        return operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found " + id));
    }

    private Account getAccount(String bankName) {
        return accountRepository.findAllByBankLike(bankName)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Account not found " + bankName));
    }
}
