package mza.thy.deposit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.reminder.EmailDepositReminder;
import mza.thy.repository.DepositRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
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
class DepositReminder {
    private final DepositRepository depositRepository;
    private final Clock clock;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Value("${deposit.reminder.days}")
    private Integer days;

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "0 0 0/8 ? * *")
    @Transactional(readOnly = true)
    public List<DepositDto> getReminders() {
        List<DepositDto> result = buildReminderList();
        log.debug("Processing deposits' reminders for {} deposits finishing within {} days.", result.size(), days);
        sendEvent(result);
        return result;
    }

    @Transactional(readOnly = true)
    public String getReminderInfo() {
        return buildReminderInfo(buildReminderList());
    }

    private List<DepositDto> buildReminderList() {
        var endDateFrom = LocalDate.now(clock).minusDays(days);
        var endDateTo = LocalDate.now(clock).plusDays(days);
        return depositRepository.findAllToRemind(endDateFrom, endDateTo)
                .map(DepositDto::convert)
                .collect(Collectors.toList());
    }

    private void sendEvent(List<DepositDto> finishingDeposits) {
        applicationEventPublisher.publishEvent(new EmailDepositReminder(buildReminderInfo(finishingDeposits)));
    }

    private String buildReminderInfo(List<DepositDto> finishingDeposits) {
        if (finishingDeposits.size() > 0) {
            return finishingDeposits.stream()
                    .map(this::convert)
                    .reduce((x, y) -> x + ", \n" + y)
                    .get();
        }
        return null;
    }

    private String convert(DepositDto depositDto) {
        return ("Deposit:" + depositDto.getId())
                .concat("," + depositDto.getBankName())
                .concat("," + depositDto.getEndDate())
                .concat("," + depositDto.getAmount());
    }
}
