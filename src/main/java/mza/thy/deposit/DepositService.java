package mza.thy.deposit;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Deposit;
import mza.thy.domain.filter.FilterParams;
import mza.thy.repository.DepositRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepositService {

    private final DepositRepository depositRepository;

    @Transactional(readOnly = true)
    public long getTotal() {
        return depositRepository.count();
    }

    @Transactional(readOnly = true)
    public List<DepositDto> getDepositList(FilterParams filterParams, @NotNull Pageable page) {
        if (Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterBank())
                || Objects.nonNull(filterParams.getFilterDate())
                || Objects.nonNull(filterParams.getFilterAmount()))
        ) {
            return doFilter(filterParams);
        }
        return depositRepository.findTotalAll(page)
                .map(DepositDto::convert)
                .collect(Collectors.toList());
    }

    private List<DepositDto> doFilter(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter deposit by id {}", filterParams.getFilterId());
            return depositRepository.findById(filterParams.getFilterId())
                    .map(DepositDto::convert)
                    .map(List::of)
                    .orElse(Collections.emptyList());
        }
        if (Objects.nonNull(filterParams.getFilterBank())) {
            log.debug("Filter deposit by bank {}", filterParams.getFilterBank());
            return depositRepository.findAllByBankLike(filterParams.getFilterBank().getValue())
                    .map(DepositDto::convert)
                    .collect(Collectors.toList());
        }

        if (Objects.nonNull(filterParams.getFilterDate())) {
            log.debug("Filter deposit by date {}", filterParams.getFilterDate());
            return depositRepository.findAllByDate(filterParams.getFilterDate())
                    .map(DepositDto::convert)
                    .collect(Collectors.toList());
        }
        if (Objects.nonNull(filterParams.getFilterAmount())) {
            log.debug("Filter deposit by amount {}", filterParams.getFilterAmount());
            return depositRepository.findAllByAmount(filterParams.getFilterAmount())
                    .map(DepositDto::convert)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    Page<DepositDto> getDepositPage(Pageable pageable) {
        var page = depositRepository.findAll(pageable);
        return page.map(DepositDto::convert);
    }

    @Transactional(readOnly = true)
    DepositDto getDepositDto(Long id) {
        var result = depositRepository.findById(id)
                .map(DepositDto::convert)
                .orElseThrow(() -> new RuntimeException("Deposit not found " + id));
        log.debug("Get deposit {}", result);
        return result;
    }


    @Transactional
    void saveDeposit(DepositDto depositDto) {
        if (Objects.nonNull(depositDto.getId())) {
            updateDeposit(depositDto.getId(), depositDto);
        } else {
            saveNewOutcome(depositDto);
        }
    }

    private Deposit saveNewOutcome(DepositDto depositDto) {
        log.debug("Saving new deposit {}", depositDto);
        return depositRepository.save(depositDto.convert());
    }

    @Transactional
    void deleteOutcome(Long id) {
        depositRepository.deleteById(id);
        log.debug("Deleted deposit {}", id);
    }

    Deposit getDeposit(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deposit not found " + id));
    }

    private Deposit updateDeposit(Long id, DepositDto changedDepositDto) {
        var deposit = getDeposit(id);
        deposit.setAmount(changedDepositDto.getAmount());
        deposit.setDate(changedDepositDto.getDate());
        deposit.setBank(changedDepositDto.getBankName());
    //    deposit.setPeriod(changedDepositDto.getPeriod());
        log.debug("Update deposit {}", changedDepositDto);
        return depositRepository.save(deposit);
    }

}
