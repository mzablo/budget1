package mza.thy.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UtilsController {
    private final UtilsService utilsService;

    @GetMapping("utils")
    public String getResult(Model model) {
        model.addAttribute("info", utilsService.getInfo());
        model.addAttribute("error", utilsService.getError());
        return "utils";
    }
}
