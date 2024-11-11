package mza.thy.repository;

import mza.thy.common.MonthlyBalanceHelperDto;
import mza.thy.common.YearlyBalanceHelperDto;
import mza.thy.domain.Income;
import mza.thy.filter.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public interface IncomeRepository extends JpaRepository<Income, Long>,
        FilterCommonType<Income>, FilterNameType<Income>, FilterDescriptionType<Income>, FilterDateType<Income>, FilterAmountType<Income>, FilterBankNameType<Income> {

    @Override
    default FilterHandler<Income> getFilter() {
        var filter = new FilterHandler<Income>(this);
        filter.setNameFilter(this);
        filter.setDateFilter(this);
        filter.setAmountFilter(this);
        filter.setDescriptionFilter(this);
        filter.setBankFilter(this);
        return filter;
    }

    @Modifying
    @Transactional
    @Query("delete from Income I where I.operation.id =:operationId")
    int deleteByOperation_Id(Long operationId);

    Optional<Income> findByOperation_Id(Long operationId);

    @Query(value = "SELECT sum(amount) FROM Income")
    BigDecimal sumIncome();

    @Query(value = "select new mza.thy.common.YearlyBalanceHelperDto(i.year, sum(i.amount)) " +
            " from Income i " +
            " group by i.year " +
            " order by i.year desc ")
    List<YearlyBalanceHelperDto> yearlyBalance();

    @Query(value = "select new mza.thy.common.MonthlyBalanceHelperDto(i.year, i.month, sum(i.amount)) " +
            " from Income i " +
            " group by i.year, i.month order by i.year desc, i.month desc")
    List<MonthlyBalanceHelperDto> monthlyBalance();

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.name like :name order by id desc")
    Stream<Income> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.date =:date order by id desc")
    Stream<Income> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.year =:year order by id desc")
    Stream<Income> findAllByYear(int year);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.month =:month order by id desc")
    Stream<Income> findAllByMonth(int month);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.month =:month and I.year =:year order by id desc")
    Stream<Income> findAllByYearAndMonth(int year, int month);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.description like :description order by id desc")
    Stream<Income> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.amount =:amount order by id desc")
    Stream<Income> findAllByAmount(BigDecimal amount);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE A.bank like :bankName order by id desc")
    Stream<Income> findAllByBankLike(@Param("bankName") String bankName);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A")
    Stream<Income> findTotalAll(Pageable pageable);
//    Page<Income> findAllX(Pageable pageable);
}
