package mza.thy.income;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping("income/list")
    public IncomeListDto getIncomeList() {
        return incomeService.getIncomeList();
    }
}
