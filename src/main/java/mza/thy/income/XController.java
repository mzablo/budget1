package mza.thy.income;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class XController {
    private final IncomeService incomeService;

    @GetMapping("x/income/list")
    public String getIncomeList(Model model) {
        var list=incomeService.getIncomeList();
        model.addAttribute("incomeList", list.getIncomeList());
        return "income-list";
    }
}
