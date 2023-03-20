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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class UtilsService {
    private final static String EXAMPLE = "income=amount:1000;name:pensja;description:firma";
    private final static String NAME = "name";
    private final static String AMOUNT = "amount";
    private final static String PRICE = "price";
    private final static String DESCRIPTION = "description";
    private final static String COUNTER = "counter";
    private final static String CATEGORY = "category";
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

    private Outcome createOutcome(LocalDate date, String template) {
        Map<String, String> values = createValueMap(template);
        if (Objects.isNull(values) || values.size() < 2) {
            error = "Outcome template should have at least 2 values, example " + EXAMPLE;
        }
        var outcome = new Outcome();
        outcome.setPrice(map(values.get(PRICE)));
        outcome.setCounter(1);
        outcome.setDate(date);
        outcome.setName(values.get(NAME));
        outcome.setCategory(values.get(CATEGORY));
        outcome.setDescription(values.get(DESCRIPTION));
        outcome = outcomeRepository.save(outcome);
        log.debug("Created outcome {}", outcome);
        return outcome;
    }

    private void createIncome(LocalDate date, String template) {
        Map<String, String> values = createValueMap(template);
        if (Objects.isNull(values) || values.size() < 2) {
            error = "Income template should have at least 2 values, example " + EXAMPLE;
        }
        var income = new Income();
        income.setAmount(map(values.get(AMOUNT)));
        income.setDate(date);
        income.setName(values.get(NAME));
        income.setDescription(values.get(DESCRIPTION));

        income = incomeRepository.save(income);
        log.debug("Created income {}", income);
    }

    private Map<String, String> createValueMap(String template) {
        String[] afterEqual = template.split("=");
        if (afterEqual.length < 1) {
            error = "Template should have values after =, example " + EXAMPLE;
            return null;
        }
        return Arrays.stream(afterEqual[1].split(";"))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(e -> e[0], e -> e[1]));
    }

    private BigDecimal map(String string) {
        if (Objects.isNull(string)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(string);
        } catch (Exception e) {
            error = "Amount " + string + " cannot be parsed";
        }
        return BigDecimal.ZERO;
    }
}
