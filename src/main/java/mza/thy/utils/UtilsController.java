package mza.thy.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.Clock;
import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UtilsController {
    private final ApplicationContext context;
    private final UtilsService utilsService;
    private final Clock clock;

    @GetMapping("utils")
    public String getResult(Model model) {
        model.addAttribute("utilsDto", new UtilsDto(utilsService.getAllTemplates(), LocalDate.now(clock)));
        model.addAttribute("error", utilsService.getError());
        model.addAttribute("defDate", LocalDate.now(clock));
        return "utils";
    }

    @PostMapping("utils")
    public String save(Model model, UtilsDto utilsDto) {
        utilsService.saveTemplates(utilsDto.getTemplates());
        model.addAttribute("utilsDto", new UtilsDto(utilsService.getAllTemplates(), LocalDate.now(clock)));
        model.addAttribute("error", utilsService.getError());
        model.addAttribute("defDate", LocalDate.now(clock));
        return "utils";
    }

    @PostMapping("utils/generate-outcome")
    public String generateOutcome(Model model, UtilsDto utilsDto) {
        utilsService.generateOutcome(utilsDto);
        model.addAttribute("utilsDto", new UtilsDto(utilsService.getAllTemplates(), LocalDate.now(clock)));
        model.addAttribute("error", utilsService.getError());
        model.addAttribute("defDate", LocalDate.now(clock));
        return "utils";
    }

    @PostMapping("utils/generate-income")
    public String generateIncome(Model model, UtilsDto utilsDto) {
        utilsService.generateIncome(utilsDto);
        model.addAttribute("utilsDto", new UtilsDto(utilsService.getAllTemplates(), LocalDate.now(clock)));
        model.addAttribute("error", utilsService.getError());
        model.addAttribute("defDate", LocalDate.now(clock));
        return "utils";
    }

    @GetMapping("exit")
    public String exit() {
        int exitCode = SpringApplication.exit(context, () -> 0);
        System.exit(exitCode);
        return "";
    }
}
