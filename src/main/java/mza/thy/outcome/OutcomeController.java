package mza.thy.outcome;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.account.AccountFacade;
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

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OutcomeController {
    private static final String pageNumberDefault = "0";
    private static final String pageSizeDefault = "100";
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
    private final OutcomeService outcomeService;
    private final AccountFacade accountService;
    private final SummaryController summaryController;

    @GetMapping("outcome")
    public String getOutcomeList(Model model,
                                 FilterParams filterParams,
                                 @RequestParam(defaultValue = "date") String sortField,
                                 @RequestParam(required = false) Sort.Direction sortDirection,
                                 @RequestParam(defaultValue = pageNumberDefault) int pageNumber,
                                 @RequestParam(defaultValue = pageSizeDefault) int pageSize
    ) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        var outcomePage = outcomeService.getOutcomeList(filterParams, PageRequest.of(pageNumber, pageSize, sortDirection, sortField));
        model.addAttribute("outcomeList", outcomePage);
        model.addAttribute("outcomeDto", new OutcomeDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("filterParams", new FilterParams());
        model.addAttribute("categoryList", outcomeService.getCategoryList());

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("total", outcomeService.getTotal());
        summaryController.getSummary(model);
        return "outcome-list";
    }

    @GetMapping("/outcome/{id}")
    public String getOutcomeListWithGivenIncome(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), sort)));
        model.addAttribute("outcomeDto", outcomeService.getOutcomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());

        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", outcomeService.getTotal());
        return "outcome-list";
    }

    @GetMapping("/outcome/delete/{id}")
    public String getOutcomeDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("outcomeDto", outcomeService.getOutcomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        return "outcome-delete";
    }

    @PostMapping("/outcome/delete/{id}")
    public String deleteOutcome(Model model, @PathVariable("id") Long id) {
        outcomeService.deleteOutcome(id);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("outcomeDto", new OutcomeDto());
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", outcomeService.getTotal());
        return "outcome-list";
    }

    @PostMapping("/outcome")
    public String saveOutcome(Model model, OutcomeDto outcome) {
        var id = outcomeService.saveOutcome(outcome);
        model.addAttribute("outcomeList", outcomeService.getOutcomeList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("outcomeDto", outcomeService.getOutcomeDto(id));
        model.addAttribute("accountList", accountService.getAccountList());
        model.addAttribute("categoryList", outcomeService.getCategoryList());
        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", outcomeService.getTotal());
        return "outcome-list";
    }
}
