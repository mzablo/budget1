package mza.thy.outcome;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.account.AccountService;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OutcomeController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "date");
    private final OutcomeService outcomeService;
    private final AccountService accountService;

    @GetMapping("outcome")
    public String getOutcomeList(Model model,
                                 FilterParams filterParams,
                                 @RequestParam(defaultValue = "date") String sortField,
                                 @RequestParam(required = false) Sort.Direction sortDirection) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(filterParams, Sort.by(sortDirection, sortField)));
        model.addAttribute("outcomeDto", new OutcomeDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("filterParams", new FilterParams());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-list";
    }

    @GetMapping("/outcome/{id}")
    public String getOutcomeListWithGivenIncome(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, sort));
        model.addAttribute("outcomeDto", outcomeService.getOutcomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-list";
    }

    @GetMapping("/outcome/delete/{id}")
    public String getOutcomeDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, defaultSort));
        model.addAttribute("outcomeDto", outcomeService.getOutcomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-delete";
    }

    @PostMapping("/outcome/delete/{id}")
    public String deleteOutcome(Model model, @PathVariable("id") Long id) {
        outcomeService.deleteOutcome(id);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, defaultSort));
        model.addAttribute("outcomeDto", new OutcomeDto());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-list";
    }

    @PostMapping("/outcome")
    public String saveOutcome(Model model, OutcomeDto outcome) {
        outcomeService.saveOutcome(outcome);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, defaultSort));
        model.addAttribute("outcomeDto", new OutcomeDto());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-list";
    }
}
