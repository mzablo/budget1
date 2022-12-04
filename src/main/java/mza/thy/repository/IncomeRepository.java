package mza.thy.repository;

import mza.thy.domain.Income;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Modifying
    @Transactional
        @Query("delete from Income I where I.operation.id =:operationId")
    int deleteByOperation_Id(Long operationId);

    Optional<Income> findByOperation_Id(Long operationId);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.name like :name")
    Stream<Income> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.date =:date")
    Stream<Income> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.description like :description")
    Stream<Income> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.amount =:amount")
    Stream<Income> findAllByAmount(BigDecimal amount);

    @Query(value = "SELECT I FROM Income I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A")
    Stream<Income> findTotalAll(Sort sort);
//    Page<Income> findAllX(Pageable pageable);
}
