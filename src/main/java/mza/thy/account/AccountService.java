package mza.thy.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.OperationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationRepository operationRepository;

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountList() {
        var result = accountRepository.findAll()
                .stream()
                .map(AccountDto::convertToDto)
                .collect(Collectors.toList());
        result.add(0, new AccountDto());
        return result;
    }

    @Transactional(readOnly = true)
    List<AccountDto> getAccountList(FilterParams filterParams, Sort sort) {
        if (Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterName())
                || Objects.nonNull(filterParams.getFilterBank()))
        ) {
            return doFilter(filterParams);
        }
        return Optional.ofNullable(sort).map(s -> accountRepository.findAll(sort))
                .orElse(accountRepository.findAll())
                .stream()
                .map(AccountDto::convertToDto)
                .collect(Collectors.toList());
    }

    private List<AccountDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter account by id {}", filterParams.getFilterId());
            return accountRepository.findById(filterParams.getFilterId())
                    .map(AccountDto::convertToDto)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter account by name {}", filterParams.getFilterName());
            return accountRepository.findAllByNameLike(filterParams.getFilterName().getValue())
                    .stream()
                    .map(AccountDto::convertToDto)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterBank())) {
            log.debug("Filter account by bank {}", filterParams.getFilterBank());
            return accountRepository.findAllByBankLike(filterParams.getFilterBank().getValue())
                    .stream()
                    .map(AccountDto::convertToDto)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    Page<AccountDto> getAccountPage(Pageable pageable) {
        var page = accountRepository.findAll(pageable);
        return page.map(AccountDto::convertToDto);
    }

    @Transactional(readOnly = true)
    AccountDto getAccount(Long id) {
        var result = accountRepository.findById(id)
                .map(AccountDto::convertToDto)
                .orElseThrow(() -> new RuntimeException("Account not found " + id));
        log.debug("Get account {}", result);
        return result;
    }

    @Transactional
    void saveAccount(AccountDto accountDto) {
        if (Objects.nonNull(accountDto.getId())) {
            updateAccount(accountDto.getId(), accountDto);
        } else {
            saveNewAccount(accountDto);
        }
    }

    private void saveNewAccount(AccountDto accountDto) {
        if (StringUtils.hasLength(accountDto.getName())) {
            log.debug("Saving new account {}", accountDto);
            accountRepository.save(accountDto.convert());
        } else {
            log.debug("Not saved - empty account {}", accountDto);
        }
    }

    @Transactional
    String deleteAccount(Long id) {
        if (operationRepository.existsByAccountId(id)) {
            log.debug("Cannot delete - operations exists for account {}", id);
            return "Cannot delete - operations exists for account " + id;
        }
        accountRepository.deleteById(id);
        log.debug("Deleted account {}", id);
        return null;
    }

    private void updateAccount(Long id, AccountDto accountDto) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found " + id));
        account.setBank(accountDto.getBank());
        account.setName(accountDto.getName());
        log.debug("Update account {}", accountDto);
        accountRepository.save(account);
    }

}
