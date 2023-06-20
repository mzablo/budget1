package mza.thy.deposit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
//przy dodaniu lokaty dwie sie zalozyly
@Controller
@RequiredArgsConstructor
@Slf4j
class DepositController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "id");
    private final DepositService depositService;
    private final DepositReminder depositReminder;

    @GetMapping("deposit")
    public String getDepositList(Model model,
                                 FilterParams filterParams,
                                 @RequestParam(defaultValue = "id") String sortField,
                                 @RequestParam(required = false) Sort.Direction sortDirection
    ) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        depositReminder.getReminders();//todo
        var depositList = depositService.getDepositList(filterParams, Sort.by(sortDirection, sortField));
        model.addAttribute("depositList", depositList);
        model.addAttribute("activeDepositList", getActiveDepositList(depositList));
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());

        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());

        return "deposit-list";
    }

    @GetMapping("/deposit/process")
    public String getDepositProcessConfirmationView(Model model) {
        model.addAttribute("process", depositService.getProcessInfo());
        return "deposit-process";
    }

    @PostMapping("/deposit/process")
    public String process(Model model) {
        depositService.process();
        model.addAttribute("depositList", depositService.getDepositList(new FilterParams(), defaultSort));
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());
        return "deposit-list";
    }

    @GetMapping("/deposit/{id}")
    public String getDepositListWithGivenDeposit(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        var depositList = depositService.getDepositList(new FilterParams(), sort);
        model.addAttribute("depositList", depositList);
        model.addAttribute("depositDto", depositService.getDepositDto(id));
        model.addAttribute("activeDepositList", getActiveDepositList(depositList));

        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());
        return "deposit-list";
    }

    @GetMapping("/deposit/delete/{id}")
    public String getDepositDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("depositList", depositService.getDepositList(null, defaultSort));
        model.addAttribute("depositDto", depositService.getDepositDto(id));
        return "deposit-delete";
    }

    @PostMapping("/deposit/delete/{id}")
    public String deleteDeposit(Model model, @PathVariable("id") Long id) {
        depositService.deleteDeposit(id);
        model.addAttribute("depositList", depositService.getDepositList(new FilterParams(), defaultSort));
        model.addAttribute("depositDto", new DepositDto());
        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());
        return "deposit-list";
    }

    @PostMapping("/deposit")
    public String saveDeposit(Model model, DepositDto deposit) {
        var result = depositService.saveDeposit(deposit);
        model.addAttribute("depositList", depositService.getDepositList(new FilterParams(), defaultSort));
        model.addAttribute("depositDto", result);
        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());
        return "deposit-list";
    }

    private List<DepositDto> getActiveDepositList(List<DepositDto> depositList) {
        return depositList.stream()
                .filter(DepositDto::getActive)
                .sorted(Comparator.comparing(DepositDto::getEndDate))
                .collect(Collectors.toList());
    }
}
