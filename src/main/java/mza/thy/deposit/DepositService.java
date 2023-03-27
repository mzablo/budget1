package mza.thy.deposit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Deposit;
import mza.thy.domain.DepositPeriod;
import mza.thy.domain.filter.FilterParams;
import mza.thy.filter.FilterHandler;
import mza.thy.repository.DepositRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class DepositService {
    private final Clock clock;
    private final DepositRepository depositRepository;
    private FilterHandler<Deposit> filter;

    public DepositService(Clock clock, DepositRepository depositRepository) {
        this.clock = clock;
        this.depositRepository = depositRepository;
        this.filter = depositRepository.getFilter();
    }

    @Transactional(readOnly = true)
    public Process getProcessInfo() {
        List<String> closedResult = depositRepository.findAllToProcess(LocalDate.now(clock))
                .map(d -> d.getId() + " " + d.getTotalAmount()+" "+d.getBank())
                .collect(Collectors.toList());
        var prolongedResult = depositRepository.findAllToProcess(LocalDate.now(clock))
                .filter(Deposit::getProlonged)
                .map(d -> d.getId() + " " + d.getTotalAmount() + " " + d.getEndDate()+" "+d.getBank())
                .collect(Collectors.toList());
        return new Process(closedResult, prolongedResult);
    }

    @Transactional
    public void process() {
        depositRepository.findAllToProcess(LocalDate.now(clock))
                .forEach(this::closeDeposit);
    }

    private void closeDeposit(Deposit deposit) {
        deposit.setActive(false);
        if (deposit.getProlonged()) {
            prolongation(deposit);
        }
        log.debug("Closing {} ", deposit.getId());
        depositRepository.save(deposit);
    }

    private void prolongation(Deposit deposit) {
        Deposit newDeposit = new Deposit(deposit.getTotalAmount(), deposit.getEndDate(),
                StringUtils.abbreviate(deposit.getDescription() + " id: " + deposit.getId(), 200),
                deposit.getPercent(), deposit.getPeriod(), deposit.getBank(), true, true);
        newDeposit = depositRepository.save(newDeposit);
        log.debug("Prolongation {}, new {} with amount {} ", deposit.getId(), newDeposit.getId(), newDeposit.getAmount());
    }

    List<String> getPeriodList() {
        return DepositPeriod.getAllPriods();
    }

    @Transactional(readOnly = true)
    public long getTotal() {
        return depositRepository.count();
    }

    @Transactional(readOnly = true)
    public List<DepositDto> getDepositList(FilterParams filterParams, Sort sort) {
        if (FilterParams.isFilled(filterParams)) {
            return doFilter(filterParams);
        }
        return depositRepository.findAll(sort)
                .stream()
                .map(DepositDto::convert)
                .collect(Collectors.toList());
    }

    private List<DepositDto> doFilter(FilterParams filterParams) {
        return filter.getFiltered(filterParams)
                .map(DepositDto::convert)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DepositDto getDepositDto(Long id) {
        var result = depositRepository.findById(id)
                .map(DepositDto::convert)
                .orElseThrow(() -> new RuntimeException("Deposit not found " + id));
        log.debug("Get deposit {}", result);
        return result;
    }

    @Transactional
    public DepositDto saveDeposit(DepositDto depositDto) {
        if (Objects.nonNull(depositDto.getId())) {
            return DepositDto.convert(updateDeposit(depositDto.getId(), depositDto));
        } else {
            return DepositDto.convert(saveNewDeposit(depositDto));
        }
    }

    private Deposit saveNewDeposit(DepositDto depositDto) {
        log.debug("Saving new deposit {}", depositDto);
        return depositRepository.save(depositDto.convert());
    }

    @Transactional
    public void deleteDeposit(Long id) {
        depositRepository.deleteById(id);
        log.debug("Deleted deposit {}", id);
    }

    Deposit getDeposit(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposit not found " + id));
    }

    private Deposit updateDeposit(Long id, DepositDto changedDepositDto) {
        var deposit = getDeposit(id);
        deposit.update(changedDepositDto.getAmount()
                , changedDepositDto.getDate()
                , changedDepositDto.getInterest()
                , changedDepositDto.getDescription()
                , changedDepositDto.getPercent()
                , changedDepositDto.getPeriod()
                , changedDepositDto.getBankName()
                , changedDepositDto.getActive()
                , changedDepositDto.getProlonged()
        );
        log.debug("Update deposit {}", deposit);
        return depositRepository.save(deposit);
    }
}
