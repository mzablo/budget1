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
        buildModel(model, filterParams, new DepositDto(), Sort.by(sortDirection, sortField));

        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());
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
        buildModel(model, new FilterParams(), new DepositDto(), defaultSort);
        return "deposit-list";
    }

    @GetMapping("/deposit/{id}")
    public String getDepositListWithGivenDeposit(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        buildModel(model, new FilterParams(), depositService.getDepositDto(id), Optional.ofNullable(sort).orElse(defaultSort));
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
        buildModel(model, new FilterParams(), new DepositDto(), defaultSort);
        return "deposit-list";
    }

    @PostMapping("/deposit")
    public String saveDeposit(Model model, DepositDto deposit) {
        var result = depositService.saveDeposit(deposit);
        buildModel(model, new FilterParams(), result, defaultSort);
        return "deposit-list";
    }

    private void buildModel(Model model, FilterParams filterParams, DepositDto result, Sort sort) {
        var depositList = depositService.getDepositList(filterParams, sort);
        model.addAttribute("depositList", depositList);
        model.addAttribute("depositDto", result);
        model.addAttribute("total", depositService.getTotal());
        model.addAttribute("periodList", depositService.getPeriodList());
        model.addAttribute("activeDepositList", getActiveDepositList(depositList));
        model.addAttribute("reminder", depositReminder.getReminderInfo());
    }

    private List<DepositDto> getActiveDepositList(List<DepositDto> depositList) {
        return depositList.stream()
                .filter(DepositDto::getActive)
                .sorted(getComparing())
                .collect(Collectors.toList());
    }

    private Comparator<DepositDto> getComparing() {
        return Comparator.comparing(DepositDto::getEndDate);
    }
}
