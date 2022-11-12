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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class XController {
    private final IncomeService incomeService;

    @GetMapping("income")
    public String getIncomeList(Model model,
                                String filterName,
                                @RequestParam(defaultValue = "date") String sortField,
                                @RequestParam(required = false) Sort.Direction sortDirection) {
        log.debug("mzaa " + filterName);
        var filterParams = FilterParams.builder()
                .filterName(filterName)
                .build();
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        model.addAttribute("incomeList", incomeService.getIncomeList(filterParams, Sort.by(sortDirection, sortField)));
        model.addAttribute("incomeDto", new IncomeDto());
        //model.addAttribute("filterName", "nam");
        model.addAttribute("isAscending", sortDirection.isAscending());
        return "income-list";
    }

    @PostMapping("income/filter")
    public String getIncomeList(Model model,
                                String filterName) {
        var filterParams = FilterParams.builder()
                .filterName(filterName)
                .build();
        log.debug("Filter  " + filterName);

        model.addAttribute("incomeList", incomeService.getIncomeList(filterParams,null));
        model.addAttribute("incomeDto", new IncomeDto());
        //model.addAttribute("filterName", "nam");
        return "income-list";
    }

    @GetMapping("/income/{id}")
    public String getIncomeListWithGivenIncome(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(Sort.by(Sort.Direction.DESC, "date"));
        model.addAttribute("incomeList", incomeService.getIncomeList(null, sort));
        model.addAttribute("incomeDto", incomeService.getIncome(id));
        return "income-list";
    }

    @GetMapping("/income/delete/{id}")
    public String getIncomeDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("incomeList", incomeService.getIncomeList(null, null));
        model.addAttribute("incomeDto", incomeService.getIncome(id));
        return "income-delete";
    }

    @PostMapping("/income/delete/{id}")
    public String deleteIncome(Model model, @PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
        model.addAttribute("incomeList", incomeService.getIncomeList(null, null));
        model.addAttribute("incomeDto", new IncomeDto());
        return "income-list";
    }


    @PostMapping("/income")
    public String saveIncome(Model model, IncomeDto income) {
        incomeService.saveIncome(income);
        model.addAttribute("incomeList", incomeService.getIncomeList(null, null));
        model.addAttribute("incomeDto", new IncomeDto());
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

    /*
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
    */
}
