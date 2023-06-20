package mza.thy.chart;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

//https://medium.com/@reachansari/google-chart-example-with-spring-boot-cd25ead09bd3
//https://stackoverflow.com/questions/69686196/draw-pie-chart-using-spring-boot-thymeleaf-js-highchart-but-cant
//https://www.youtube.com/watch?v=fKjWyiS3mtM
//https://www.wimdeblauwe.com/blog/2021/01/05/using-google-charts-with-thymeleaf/
@Controller
@RequiredArgsConstructor
@Slf4j
class ChartController {
    private final ChartService chartService;

    @GetMapping("chart")
    public String getChart(Model model) {
        model.addAttribute("balanceData", chartService.getYearlyBalance());
        model.addAttribute("outcomeByCategoryData", chartService.getLastOutcomePerCategory(null,null));//getChartData1());
        return "chart-list";
    }
}
