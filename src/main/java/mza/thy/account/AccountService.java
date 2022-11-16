package mza.thy.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.AccountRepository;
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
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountList(FilterParams filterParams, Sort sort) {
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
                .map(AccountDto::convert)
                .collect(Collectors.toList());
    }

    private List<AccountDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter account by id " + filterParams.getFilterId());
            return accountRepository.findById(filterParams.getFilterId())
                    .map(AccountDto::convert)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterName())) {
            log.debug("Filter account by name " + filterParams.getFilterName());
            return accountRepository.findAllByNameLike(filterParams.getFilterName().getValue())
                    .map(AccountDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterBank())) {
            log.debug("Filter account by bank " + filterParams.getFilterBank());
            return accountRepository.findAllByBankLike(filterParams.getFilterBank().getValue())
                    .map(AccountDto::convert)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    Page<AccountDto> getAccountPage(Pageable pageable) {
        var page = accountRepository.findAll(pageable);
        return page.map(AccountDto::convert);
    }

    @Transactional(readOnly = true)
    AccountDto getAccount(Long id) {
        return accountRepository.findById(id)
                .map(AccountDto::convert)
                .orElseThrow(() -> new RuntimeException("Account not found " + id));
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
        if (Objects.nonNull(accountDto.getName())) {
            log.debug("Saving new account " + accountDto);
            accountRepository.save(accountDto.convert());
        } else {
            log.debug("Not saved - empty account " + accountDto);
        }
    }

    @Transactional
    void deleteAccount(Long id) {
        accountRepository.deleteById(id);
        log.debug("Deleted account " + id);
    }

    private void updateAccount(Long id, AccountDto accountDto) {
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brak account " + id));
        account.setBank(accountDto.getBank());
        log.debug("Update account " + accountDto);
        accountRepository.save(account);
    }

}
