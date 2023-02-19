package mza.thy.domain.filter;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

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
    private Integer filterYear;
    private Integer filterMonth;
    private FilterString filterName;
    private FilterString filterCategory;
    private BigDecimal filterAmount;
    private FilterString filterDescription;
    private FilterString filterBank;
    private FilterString filterWho;

    public static boolean isFilled(FilterParams filterParams) {
        return Objects.nonNull(filterParams)
                && (Objects.nonNull(filterParams.getFilterId())
                || Objects.nonNull(filterParams.getFilterCategory())
                || Objects.nonNull(filterParams.getFilterWho())
                || Objects.nonNull(filterParams.getFilterName())
                || Objects.nonNull(filterParams.getFilterBank())
                || Objects.nonNull(filterParams.getFilterDate())
                || Objects.nonNull(filterParams.getFilterMonth())
                || Objects.nonNull(filterParams.getFilterYear())
                || Objects.nonNull(filterParams.getFilterDescription())
                || Objects.nonNull(filterParams.getFilterAmount()));
    }

}
