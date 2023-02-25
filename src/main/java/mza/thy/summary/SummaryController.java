package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.TabType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SummaryController {
    private final SummaryService summaryService;

    //@GetMapping("summary")
    public String getSummary(Model model, TabType tabType) {
        var summary = summaryService.getSummary();
        model.addAttribute("totalIncome", summary.getTotalIncome());
        model.addAttribute("totalOutcome", summary.getTotalOutcome());
        model.addAttribute("balance", summary.getBalance());
        model.addAttribute("currentTab", tabType);
        return "summary";
    }
}
