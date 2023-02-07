package mza.thy.domain.filter;

import lombok.*;
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
    private FilterString filterName;
    private FilterString filterCategory;
    private BigDecimal filterAmount;
    private FilterString filterDescription;
    private FilterString filterBank;
    private FilterString filterWho;
}
