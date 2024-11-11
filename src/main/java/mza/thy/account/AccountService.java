package mza.thy.account;

import lombok.extern.slf4j.Slf4j;
import mza.thy.common.CacheService;
import mza.thy.domain.Account;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.AccountRepository;
import mza.thy.repository.DepositRepository;
import mza.thy.repository.OperationRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
class AccountService implements AccountFacade {

    private final AccountRepository accountRepository;
    private final DepositRepository depositRepository;
    private final OperationRepository operationRepository;
    private final DecimalFormat decimalFormat;
    private final CacheService cacheService;
    private FilterHandler<Account> filter;

    public AccountService(AccountRepository accountRepository, OperationRepository operationRepository, DecimalFormat decimalFormat, DepositRepository depositRepository, CacheService cacheService) {
        this.accountRepository = accountRepository;
        this.depositRepository = depositRepository;
        this.operationRepository = operationRepository;
        this.decimalFormat = decimalFormat;
        this.cacheService = cacheService;
        this.filter = accountRepository.getFilter();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheService.ACCOUNT_LIST)
    public List<AccountDto> getAccountList() {
        var result = accountRepository.findAll()
                .stream()
                .map(a -> AccountDto.convertToDto(a, getAccountBalance(a)))
                .collect(Collectors.toList());
        result.add(0, new AccountDto());
        return result;
    }

    @Transactional(readOnly = true)
    public List<AccountDto> getAccountList(FilterParams filterParams, Sort sort) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return Optional.ofNullable(sort).map(s -> accountRepository.findAll(sort))
                .orElse(accountRepository.findAll())
                .stream()
                .map(a -> AccountDto.convertToDto(a, getAccountBalance(a)))
                .toList();
    }

    private String getAccountBalance(Account account) {
        var balance = operationRepository.balanceByAccount(account.getId());
        return Optional.ofNullable(balance)
                .map(decimalFormat::format)
                .orElse("-");
    }

    private List<AccountDto> doFilter(FilterParams filterParams) {
        return filter.getFiltered(filterParams)
                .map(a -> AccountDto.convertToDto(a, getAccountBalance(a)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<AccountDto> getAccountPage(Pageable pageable) {
        var page = accountRepository.findAll(pageable);
        return page.map(a -> AccountDto.convertToDto(a, getAccountBalance(a)));
    }

    @Transactional(readOnly = true)
    public AccountDto getAccount(Long id) {
        var result = accountRepository.findById(id)
                .map(a -> AccountDto.convertToDto(a, getAccountBalance(a)))
                .orElseThrow(() -> new RuntimeException("Account not found " + id));
        log.debug("Get account {}", result);
        return result;
    }

    @Transactional
    public void saveAccount(AccountDto accountDto) {
        if (Objects.nonNull(accountDto.getId())) {
            updateAccount(accountDto.getId(), accountDto);
        } else {
            saveNewAccount(accountDto);
        }
        removeCache();
    }

    private void removeCache() {
        cacheService.removeCache(CacheService.ACCOUNT_LIST);
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
    public String deleteAccount(Long id) {
        if (operationRepository.existsByAccountId(id)) {
            log.debug("Cannot delete - operations exists for account {}", id);
            return "Cannot delete - operations exists for account " + id;
        }
        accountRepository.deleteById(id);
        removeCache();
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

    @Transactional(readOnly = true)
    public List<Map<String, String>> getDepositsRows() {
        return depositRepository.sumActiveDepositByBank().stream()
                .map(b -> b.getMap(decimalFormat))
                .collect(Collectors.toList());
    }
}
