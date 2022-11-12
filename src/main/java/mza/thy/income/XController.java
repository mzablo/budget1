package mza.thy.income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@Slf4j
public class XController {
    private final IncomeService incomeService;

    //confirm dletetion przez przekierowanie na strone do kasowania

    @GetMapping("income")
    public String getIncomeList(Model model) {
        model.addAttribute("incomeList", incomeService.getIncomeList());
        model.addAttribute("incomeDto", new IncomeDto());
      //  model.addAttribute("defDate", LocalDate.now());
        return "income-list";
    }

    @GetMapping("/income/{id}")
    public String incomeEdit(Model model, @PathVariable("id") Long id) {
        var income = incomeService.getIncome(id);
        model.addAttribute("incomeDto", income);
        model.addAttribute("incomeList", incomeService.getIncomeList());
        return "income-list";
    }

    private void extracted(Model model, Integer pageNumber, Integer pageSize) {
        var page = incomeService.getIncomePage(PageRequest.of(pageNumber, pageSize, Sort.Direction.DESC, "date"));
        model.addAttribute("incomeList", page.stream().toList());
        model.addAttribute("incomeDto", new IncomeDto());
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("incomePage", page);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("defDate", LocalDate.now());
    }

    @PostMapping("/income/{id}")
    public String updateIncome(Model model, @PathVariable("id") Long id, IncomeDto income) {
        if (Objects.nonNull(income.getDescription())) {
            incomeService.updateIncome(id, income);
        } else {
            log.debug("Empty object");
        }
        model.addAttribute("incomeList", incomeService.getIncomeList());
        model.addAttribute("incomeDto", new IncomeDto());
        return "income-list";
    }

    @PostMapping("/income")
    public String saveIncome(Model model, IncomeDto income) {
        if (Objects.nonNull(income.getDescription())) {
            incomeService.saveNewIncome(income);
        } else {
            log.debug("Empty object");
        }
        model.addAttribute("incomeList", incomeService.getIncomeList());
        model.addAttribute("incomeDto", new IncomeDto());
        return "income-list";
    }
}
