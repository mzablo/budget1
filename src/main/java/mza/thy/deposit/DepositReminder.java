package mza.thy.deposit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//https://blog.nazrulkabir.com/2018/04/dynamic-modal-with-spring-boot-and-thymeleaf/
@Service
@Slf4j
@RequiredArgsConstructor
public class DepositReminder {
    private final DepositRepository depositRepository;
    private final Clock clock;

    @Value("${deposit.reminder.days}")
    private Integer days;

    @Transactional(readOnly = true)
    public List<DepositDto> getReminders() {
        var endDateFrom = LocalDate.now(clock).minusDays(days);
        var endDateTo = LocalDate.now(clock).plusDays(days);
        var result= depositRepository.findAllToRemind(endDateFrom, endDateTo)
                .map(DepositDto::convert)
                .collect(Collectors.toList());
        log.debug("Finishing deposits!!! {}", result);
        return result;
    }
}
