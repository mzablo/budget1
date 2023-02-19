package mza.thy.repository;

import mza.thy.domain.Operation;
import mza.thy.filter.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

//!!!todo  zrobic graf aby pozenic pageable i fetch aby nie bylo n+1
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>,
        FilterCommonType<Operation>, FilterNameType<Operation>, FilterDescriptionType<Operation>, FilterDateType<Operation>, FilterAmountType<Operation> {

    @Override
    default FilterHandler<Operation> getFilter() {
        var filter = new FilterHandler<Operation>(this);
        filter.setNameFilter(this);
        filter.setDateFilter(this);
        filter.setAmountFilter(this);
        filter.setDescriptionFilter(this);
        return filter;
    }

    @Query(value = "SELECT O FROM Operation O LEFT JOIN FETCH O.account A " +
            "WHERE O.name like :name order by id desc")
    Stream<Operation> findAllByNameLike(String name);

    Stream<Operation> findAllByDescriptionLike(String description);

    Stream<Operation> findAllByDate(LocalDate date);
    Stream<Operation> findAllByMonth(int month);
    Stream<Operation> findAllByYear(int year);
    Stream<Operation> findAllByYearAndMonth(int year, int month);

    Stream<Operation> findAllByAmount(BigDecimal amount);

    boolean existsByAccountId(Long accountId);

    //@Query(value = "SELECT O FROM Operation O LEFT JOIN FETCH O.account A")
    Page<Operation> findAll(Pageable pageable);

    @Query(value = "SELECT sum(amount) FROM Operation O WHERE O.account.id =:accountId")
    BigDecimal balanceByAccount(Long accountId);
}
