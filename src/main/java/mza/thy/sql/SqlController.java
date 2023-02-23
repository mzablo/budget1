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
        var sqlResult=sqlService.getResult(sql);
        model.addAttribute("sqlDto", sqlResult);
        model.addAttribute("headers", sqlResult.getHeaders());
        model.addAttribute("rows", sqlResult.getRows());
        model.addAttribute("sqlList", sqlService.getSqlList());
        return "sql-list";
    }
}
