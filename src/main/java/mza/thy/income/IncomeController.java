package mza.thy.income;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class IncomeController {
    private final IncomeService incomeService;

    @GetMapping("income/list")
    public List<IncomeDto> getIncomeList() {
        return incomeService.getIncomeList(null, Sort.by(Sort.Direction.DESC, "date"));
    }
}
