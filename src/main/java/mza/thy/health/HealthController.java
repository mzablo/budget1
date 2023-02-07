package mza.thy.health;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    private final Sort defaultSort = Sort.by(Sort.Direction.DESC, "date");
    private final HealthService healthService;

    @GetMapping("health")
    public String getIllnessList(Model model,
                                 FilterParams filterParams,
                                 @RequestParam(defaultValue = "date") String sortField,
                                 @RequestParam(required = false) Sort.Direction sortDirection) {
        sortDirection = Optional.ofNullable(sortDirection)
                .orElse(Sort.Direction.DESC);
        model.addAttribute("illnessList", healthService.getIllnessList(filterParams, Sort.by(sortDirection, sortField)));
        model.addAttribute("cureList", healthService.getCureList());
        model.addAttribute("cureForIllnessList", Collections.emptyList());
        model.addAttribute("illnessDto", new IllnessDto());
        model.addAttribute("isAscending", sortDirection.isAscending());
        model.addAttribute("filterParams", new FilterParams());
        return "health-list";
    }

    @GetMapping("/health/{id}")
    public String getIllnessListWithGivenIllness(Model model, @PathVariable("id") Long id, @RequestParam(required = false) Sort sort) {
        sort = Optional.ofNullable(sort).orElse(defaultSort);
        model.addAttribute("illnessList", healthService.getIllnessList(null, sort));
        model.addAttribute("cureList", healthService.getCureList());
        model.addAttribute("cureForIllnessList", healthService.getCureForIllness(id));
        model.addAttribute("illnessDto", healthService.getIllnessDto(id));
        return "health-list";
    }

    @GetMapping("/health/delete/{id}")
    public String getIllnessDeleteConfirmationView(Model model, @PathVariable("id") Long id) {
        model.addAttribute("illnessList", healthService.getIllnessList(null, defaultSort));
        model.addAttribute("illnessDto", healthService.getIllnessDto(id));
        return "health-delete";
    }

    @PostMapping("/health/delete/{id}")
    public String deleteIIllness(Model model, @PathVariable("id") Long id) {
        healthService.deleteIllness(id);
        model.addAttribute("illnessList", healthService.getIllnessList(null, defaultSort));
        model.addAttribute("illnessDto", new IllnessDto());
        model.addAttribute("cureList", healthService.getCureList());
        model.addAttribute("cureForIllnessList", Collections.emptyList());
        return "health-list";
    }

    @PostMapping("/health")
    public String saveIllness(Model model, IllnessDto illnessDto) {
        var id = healthService.saveIllness(illnessDto);
        model.addAttribute("illnessList", healthService.getIllnessList(null, defaultSort));
        model.addAttribute("illnessDto", healthService.getIllnessDto(id));
        model.addAttribute("cureList", healthService.getCureList());
        model.addAttribute("cureForIllnessList", Collections.emptyList());
        return "health-list";
    }
}
