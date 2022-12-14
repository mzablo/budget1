package mza.thy.income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.account.AccountService;
import mza.thy.domain.filter.FilterParams;
import mza.thy.summary.SummaryController;
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

//https://www-thymeleaf-org.translate.goog/doc/articles/layouts.html?_x_tr_sl=auto&_x_tr_tl=pl&_x_tr_hl=pl
//!!! dorobic ctrls
//!!! dorobic templaty
//!!! zdrowie, investment, sql, view, wykresy?
//!!! sql - to moze per zakladka? jak updaty to brak wyniku - obsluzyc
//dzienniczek biegowy
//!!! na gorze/dole strony podsumowanie jak zmienia sie total
//!!!commitowanie jak wychodzi sie z aplikacji??
//!!!zamykanie apki z gui?
//!!!pogrubienie w menu na jakiej zakladce jestem
@Controller
@RequiredArgsConstructor
@Slf4j
public class IncomeController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "date");
    private final IncomeService incomeService;
    private final AccountService accountService;
    private final SummaryController summaryController;

    @GetMapping("income")
    public String getIncomeList(Model model,
                                FilterParams filterParams,
                                @RequestParam(defaultValue = "date") String sortField,
                                @RequestParam(required = false) Sort.Direction sortDirection) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        model.addAttribute("incomeList", incomeService.getIncomeList(filterParams, Sort.by(sortDirection, sortField)));
        model.addAttribute("incomeDto", new IncomeDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("filterParams", new FilterParams());
        summaryController.getSummary(model);
        return "income-list";
    }

    @GetMapping("/income/{id}")
    public String getIncomeListWithGivenIncome(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        model.addAttribute("incomeList", incomeService.getIncomeList(null, sort));
        model.addAttribute("incomeDto", incomeService.getIncomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        return "income-list";
    }

    @GetMapping("/income/delete/{id}")
    public String getIncomeDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("incomeList", incomeService.getIncomeList(null, defaultSort));
        model.addAttribute("incomeDto", incomeService.getIncomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        return "income-delete";
    }

    @PostMapping("/income/delete/{id}")
    public String deleteIncome(Model model, @PathVariable("id") Long id) {
        incomeService.deleteIncome(id);
        model.addAttribute("incomeList", incomeService.getIncomeList(null, defaultSort));
        model.addAttribute("incomeDto", new IncomeDto());
        model.addAttribute("accountList", accountService.getAccountList());
        return "income-list";
    }

    @PostMapping("/income")
    public String saveIncome(Model model, IncomeDto income) {
        var id = incomeService.saveIncome(income);
        model.addAttribute("incomeList", incomeService.getIncomeList(null, defaultSort));
        model.addAttribute("incomeDto", incomeService.getIncomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
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
}
