package mza.thy.repository;

import mza.thy.account.DepositDto;
import mza.thy.domain.Deposit;
import mza.thy.filter.*;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long>,
        FilterCommonType<Deposit>, FilterDateType<Deposit>, FilterAmountType<Deposit>, FilterDescriptionType<Deposit>, FilterBankNameType<Deposit> {

    @Override
    default FilterHandler<Deposit> getFilter() {
        var filter = new FilterHandler<Deposit>(this);
        filter.setDateFilter(this);
        filter.setAmountFilter(this);
        filter.setDescriptionFilter(this);
        filter.setBankFilter(this);
        return filter;
    }

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE lower(D.bank) like :name order by id desc")
    Stream<Deposit> findAllByBankLike(String name);

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE lower(D.description) like :description order by id desc")
    Stream<Deposit> findAllByDescriptionLike(String description);

    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.date =:date order by id desc")
    Stream<Deposit> findAllByDate(LocalDate date);

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE D.year =:year order by id desc")
    Stream<Deposit> findAllByYear(int year);

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE D.month =:month order by id desc")
    Stream<Deposit> findAllByMonth(int month);

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE D.year =:year and D.month =:month order by id desc")
    Stream<Deposit> findAllByYearAndMonth(int year, int month);

    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.amount =:amount order by id desc")
    Stream<Deposit> findAllByAmount(BigDecimal amount);

    List<Deposit> findAll(Sort sort);

    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.endDate <=:endDate and D.active is true")
    Stream<Deposit> findAllToProcess(LocalDate endDate);

    @Query(value = "SELECT SUM(D.amount) FROM Deposit D WHERE ACTIVE is true " +
            "AND DESCRIPTION NOT LIKE '%ADB%'")
    BigDecimal sumActiveDeposit();

    @Query(value = "SELECT SUM(D.amount) FROM Deposit D " +
            "WHERE ACTIVE is true AND DESCRIPTION LIKE '%ADB%'")
    BigDecimal sumAdbActiveDeposit();

    @Query(value = "SELECT new mza.thy.account.DepositDto(D.bank, SUM(D.amount)) FROM Deposit D " +
            "WHERE ACTIVE is true GROUP BY D.bank")
    List<DepositDto> sumActiveDepositByBank();


    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.endDate >=:endDateFrom and D.endDate <=:endDateTo and D.active is true" +
            " ORDER By D.endDate")
    Stream<Deposit> findAllToRemind(LocalDate endDateFrom, LocalDate endDateTo);
}
