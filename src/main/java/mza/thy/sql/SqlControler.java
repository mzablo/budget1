package mza.thy.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SqlControler {
    private final SqlService sqlService;

    @PostMapping("sql")
    public String getResult(Model model, SqlReqDto sql) {
        var sqlResult = sqlService.getResult(sql);
        model.addAttribute("sqlDto", sqlResult);
        model.addAttribute("headers", Optional.ofNullable(sqlResult.getHeaders()).orElse(Collections.emptyList()));
        model.addAttribute("rows", Optional.ofNullable(sqlResult.getRows()).orElse(Collections.emptyList()));
        model.addAttribute("sqls", sqlService.getSqls());
        model.addAttribute("error", sqlService.getError());
        return "sql-list";
    }

    @GetMapping("sql")
    public String getSql(Model model, SqlReqDto sql) {
        var sqlResult = sqlService.getResult(sql);
        model.addAttribute("sqlDto", sqlResult);
        model.addAttribute("headers", Optional.ofNullable(sqlResult.getHeaders()).orElse(Collections.emptyList()));
        model.addAttribute("rows", Optional.ofNullable(sqlResult.getRows()).orElse(Collections.emptyList()));
        model.addAttribute("sqls", sqlService.getSqls());
        model.addAttribute("error", sqlService.getError());
        return "sql-list";
    }
}
