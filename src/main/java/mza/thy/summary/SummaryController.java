package mza.thy.summary;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.common.TabType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SummaryController {
    private final SummaryService summaryService;

    public String getSummary(Model model) {
        var summary = summaryService.getSummary();
        model.addAttribute("totalIncome", summary.getTotalIncome());
        model.addAttribute("totalOutcome", summary.getTotalOutcome());
        model.addAttribute("balance", summary.getBalance());
        return "summary";
    }

    @GetMapping("/summary")
    public String getSummaryView(Model model) {
        var summary = summaryService.getSummary();
        model.addAttribute("summaryDto", summary);
        /*model.addAttribute("totalDeposit", summary.getTotalDeposit());
        model.addAttribute("totalAccounts", summary.getTotalAccounts());
        model.addAttribute("pocket", summary.getPocket());
        model.addAttribute("totalIncome", summary.getTotalIncome());
        model.addAttribute("totalOutcome", summary.getTotalOutcome());
        model.addAttribute("balance", summary.getBalance());
        model.addAttribute("realBalance", summary.getRealBalance());*/
        model.addAttribute("headers", summary.getHeaders());
        model.addAttribute("rows", summary.getRows());

        model.addAttribute("monthlyHeaders", summary.getMonthlyHeaders());
        model.addAttribute("monthlyRows", summary.getMonthlyRows());

        model.addAttribute("error", summaryService.getError());
        return "summary";
    }

}
