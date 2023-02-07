package mza.thy.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SqlController {
    private final SqlService sqlService;

    @GetMapping("sql")
    public String getResult(Model model, SqlDto sql) {
        model.addAttribute("sqlDto", sqlService.getResult(sql));
        return "sql-list";
    }
}
