package mza.thy.filter;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mza.thy.domain.filter.FilterParams;

import java.util.Objects;
import java.util.stream.Stream;


@Slf4j
@Setter
public class FilterHandler<T> {
    private FilterCommonType<T> commonFilter;
    private FilterNameType<T> nameFilter;
    private FilterDescriptionType<T> descriptionFilter;
    private FilterCategoryType<T> categoryFilter;
    private FilterDateType<T> dateFilter;
    private FilterAmountType<T> amountFilter;
    private FilterBankNameType<T> bankFilter;
    private FilterWhoType<T> whoFilter;

    public FilterHandler(FilterCommonType filterCommonType) {
        this.commonFilter = filterCommonType;
    }

    public Stream<T> getFiltered(FilterParams filterParams) {
        if (Objects.nonNull(filterParams.getFilterId())) {
            log.debug("Filter by id {}", filterParams.getFilterId());
            return commonFilter.findAllById(filterParams.getFilterId()).stream();
        }
        if (Objects.nonNull(filterParams.getFilterName()) && Objects.nonNull(nameFilter)) {
            log.debug("Filter by name {}", filterParams.getFilterName());
            return nameFilter.findAllByNameLike(filterParams.getFilterName().getValue());
        }
        if (Objects.nonNull(filterParams.getFilterDescription()) && Objects.nonNull(descriptionFilter)) {
            log.debug("Filter by description {}", filterParams.getFilterDescription());
            return descriptionFilter.findAllByDescriptionLike(filterParams.getFilterDescription().getValue());
        }
        if (Objects.nonNull(filterParams.getFilterCategory()) && Objects.nonNull(categoryFilter)) {
            log.debug("Filter by category {}", filterParams.getFilterCategory());
            return categoryFilter.findAllByCategoryLike(filterParams.getFilterCategory().getValue());
        }
        if (Objects.nonNull(filterParams.getFilterDate()) && Objects.nonNull(dateFilter)) {
            log.debug("Filter by date {}", filterParams.getFilterDate());
            return dateFilter.findAllByDate(filterParams.getFilterDate());
        }
        if (Objects.nonNull(filterParams.getFilterYear()) && Objects.nonNull(filterParams.getFilterMonth()) && Objects.nonNull(dateFilter)) {
            log.debug("Filter by month/year {} {}", filterParams.getFilterMonth(), filterParams.getFilterYear());
            return dateFilter.findAllByYearAndMonth(filterParams.getFilterYear(), filterParams.getFilterMonth());
        }
        if (Objects.nonNull(filterParams.getFilterYear()) && Objects.isNull(filterParams.getFilterMonth()) && Objects.nonNull(dateFilter)) {
            log.debug("Filter by year {}", filterParams.getFilterYear());
            return dateFilter.findAllByYear(filterParams.getFilterYear());
        }
        if (Objects.nonNull(filterParams.getFilterMonth()) && Objects.isNull(filterParams.getFilterYear()) && Objects.nonNull(dateFilter)) {
            log.debug("Filter by month {}", filterParams.getFilterMonth());
            return dateFilter.findAllByMonth(filterParams.getFilterMonth());
        }
        if (Objects.nonNull(filterParams.getFilterAmount()) && Objects.nonNull(amountFilter)) {
            log.debug("Filter by amount {}", filterParams.getFilterAmount());
            return amountFilter.findAllByAmount(filterParams.getFilterAmount());
        }
        if (Objects.nonNull(filterParams.getFilterBank()) && Objects.nonNull(bankFilter)) {
            log.debug("Filter by bank {}", filterParams.getFilterBank());
            return bankFilter.findAllByBankLike(filterParams.getFilterBank().getValue());
        }
        if (Objects.nonNull(filterParams.getFilterWho()) && Objects.nonNull(whoFilter)) {
            log.debug("Filter by who {}", filterParams.getFilterWho());
            return whoFilter.findAllByWhoLike(filterParams.getFilterWho().getValue());
        }
        return Stream.empty();
    }
}
