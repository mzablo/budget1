package mza.thy.repository;

import mza.thy.common.MonthlyBalanceHelperDto;
import mza.thy.common.OutcomeByCategoryHelperDto;
import mza.thy.common.YearlyBalanceHelperDto;
import mza.thy.domain.Outcome;
import mza.thy.filter.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long>,
        FilterCommonType<Outcome>, FilterNameType<Outcome>, FilterDescriptionType<Outcome>, FilterDateType<Outcome>, FilterCategoryType<Outcome>, FilterAmountType<Outcome>, FilterBankNameType<Outcome> {

    @Override
    default FilterHandler<Outcome> getFilter() {
        var filter = new FilterHandler<Outcome>(this);
        filter.setNameFilter(this);
        filter.setDateFilter(this);
        filter.setAmountFilter(this);
        filter.setCategoryFilter(this);
        filter.setDescriptionFilter(this);
        filter.setBankFilter(this);
        return filter;
    }

    @Modifying
    @Transactional
    @Query("delete from Outcome O where O.operation.id =:operationId")
    int deleteByOperation_Id(Long operationId);

    Optional<Outcome> findByOperation_Id(Long operationId);

    @Query(value = "SELECT sum(price*counter) FROM Outcome")
    BigDecimal sumOutcome();

    @Query(value = "select new mza.thy.common.YearlyBalanceHelperDto(o.year, sum(o.price*o.counter)) " +
            " from Outcome o " +
            " group by o.year " +
            " order by o.year desc ")
    List<YearlyBalanceHelperDto> yearlyBalance();

    @Query(value = "select new mza.thy.common.MonthlyBalanceHelperDto(o.year, o.month, sum(o.price*o.counter)) " +
            " from Outcome o " +
            " group by o.year, o.month order by o.year desc, o.month desc")
    List<MonthlyBalanceHelperDto> monthlyBalance();

    @Query(value = "select new mza.thy.common.OutcomeByCategoryHelperDto(o.category, sum(o.price*o.counter)) " +
            " from Outcome o " +
            " where o.year =:year and o.month =:month group by o.category")
    Stream<OutcomeByCategoryHelperDto> outcomeByCategory(int year, int month);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE lower(I.name) like :name order by id desc")
    Stream<Outcome> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.date =:date order by id desc")
    Stream<Outcome> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.year =:year order by id desc")
    Stream<Outcome> findAllByYear(int year);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.month =:month order by id desc")
    Stream<Outcome> findAllByMonth(int month);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.year =:year and I.month =:month order by id desc")
    Stream<Outcome> findAllByYearAndMonth(int year, int month);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE lower(I.description) like :description order by id desc")
    Stream<Outcome> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE lower(I.category) like :category order by id desc")
    Stream<Outcome> findAllByCategoryLike(String category);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.price =:price order by id desc")
    Stream<Outcome> findAllByAmount(BigDecimal price);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE lower(A.bank) like :bankName order by id desc")
    Stream<Outcome> findAllByBankLike(@Param("bankName") String bankName);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A")
    Stream<Outcome> findTotalAll(Pageable page);
}
