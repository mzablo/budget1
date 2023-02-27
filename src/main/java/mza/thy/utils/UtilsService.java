package mza.thy.utils;

import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.Income;
import mza.thy.domain.Outcome;
import mza.thy.repository.IncomeRepository;
import mza.thy.repository.OutcomeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
class UtilsService {
    private final Path path;
    private String error;
    private final OutcomeRepository outcomeRepository;
    private final IncomeRepository incomeRepository;

    public UtilsService(OutcomeRepository outcomeRepository, IncomeRepository incomeRepository) {
        path = Paths.get("templates.txt");
        this.outcomeRepository = outcomeRepository;
        this.incomeRepository = incomeRepository;
    }

    String getError() {
        return error;
    }

    public void saveTemplates(String templates) {
        try {
            Files.write(path, templates.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAllTemplates() {
        try {
            var result = Files.readString(path);
            //log.debug("Loaded templates {}", result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "empty";
    }

    public List<String> getAllTemplates(String key) {
        try {
            var result = Files.readAllLines(path).stream()
                    .filter(l -> l.contains(key))
                    .collect(Collectors.toList());
            log.debug("Loaded by key {} templates {}", key, result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of("empty");
    }

    public void generateOutcome(UtilsDto utilsDto) {
        log.debug("Generating outcomes from templates for date {}", utilsDto.getDate());
        getAllTemplates("outcome")
                .forEach(t -> createOutcome(utilsDto.getDate(), t));
    }

    public void generateIncome(UtilsDto utilsDto) {
        log.debug("Generating incomes from templates for date {}", utilsDto.getDate());
        getAllTemplates("income")
                .forEach(t -> createIncome(utilsDto.getDate(), t));
    }

    private Outcome createOutcome(LocalDate date, String def) {
        String[] afterEqual = def.split("=");
        if (afterEqual.length < 1) {
            error = "Template should have values after =";
            return new Outcome();
        }
        String[] values = afterEqual[1].split(" ");
        var outcome = new Outcome();
        if (values.length < 2) {
            error = "Outcome template should have at least 2 values";
            return new Outcome();
        }
        outcome.setPrice(map(values[0]));
        outcome.setCounter(1);
        outcome.setDate(date);
        outcome.setName(values[1]);
        if (values.length > 2) {
            outcome.setCategory(values[2]);
        }
        if (values.length > 3) {
            outcome.setDescription(values[3]);
        }
        outcome = outcomeRepository.save(outcome);
        log.debug("Created outcome {}", outcome);
        return outcome;
    }

    private Income createIncome(LocalDate date, String def) {
        String[] afterEqual = def.split("=");
        if (afterEqual.length < 1) {
            error = "Template should have values after =";
            return new Income();
        }
        String[] values = afterEqual[1].split(" ");
        var income = new Income();
        if (values.length < 2) {
            error = "Outcome template should have at least 2 values";
            return new Income();
        }
        income.setAmount(map(values[0]));
        income.setDate(date);
        income.setName(values[1]);
        if (values.length > 2) {
            income.setDescription(values[2]);
        }

        income = incomeRepository.save(income);
        log.debug("Created income {}", income);
        return income;
    }

    private BigDecimal map(String string) {
        try {
            return new BigDecimal(string);
        } catch (Exception e) {
            error = "Amount " + string + " cannot be parsed";
        }
        return BigDecimal.ZERO;
    }
}
