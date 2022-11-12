package mza.thy.income;

import lombok.*;
import mza.thy.domain.Income;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class FilterParams {
    private Long filterId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate filterDate;
    private String filterName;
    private BigDecimal filterAmount;
    private String filterDescription;
}
