package mza.thy.deposit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DepositController {
    private static final String pageNumberDefault = "0";
    private static final String pageSizeDefault = "100000";
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "date");
    private final DepositService depositService;
    private final SummaryController summaryController;

    @GetMapping("deposit")
    public String getDepositList(Model model,
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
        var depositList = depositService.getDepositList(filterParams, PageRequest.of(pageNumber, pageSize, sortDirection, sortField));
        model.addAttribute("depositList", depositList);
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("total", depositService.getTotal());
        summaryController.getSummary(model);
        return "deposit-list";
    }

    @GetMapping("/deposit/{id}")
    public String getDepositListWithGivenIncome(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        model.addAttribute("depositList", depositService.getDepositList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), sort)));
        model.addAttribute("depositDto", depositService.getDepositDto(id));

        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", depositService.getTotal());
        return "deposit-list";
    }

    @GetMapping("/deposit/delete/{id}")
    public String getDepositDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("depositList", depositService.getDepositList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("depositDto", depositService.getDepositDto(id));
        return "deposit-delete";
    }

    @PostMapping("/deposit/delete/{id}")
    public String deleteDeposit(Model model, @PathVariable("id") Long id) {
        depositService.deleteOutcome(id);
        model.addAttribute("depositList", depositService.getDepositList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", depositService.getTotal());
        return "deposit-list";
    }

    @PostMapping("/deposit")
    public String saveDeposit(Model model, DepositDto deposit) {
        depositService.saveDeposit(deposit);
        model.addAttribute("outcomeList", depositService.getDepositList(null, PageRequest.of(Integer.parseInt(pageNumberDefault), Integer.parseInt(pageSizeDefault), defaultSort)));
        model.addAttribute("outcomeDto", new DepositDto());
        model.addAttribute("pageNumber", 0);
        model.addAttribute("total", depositService.getTotal());
        return "deposit-list";
    }
}
