package mza.thy.repository;

import mza.thy.domain.Outcome;
import org.springframework.data.domain.Pageable;
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
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

    @Modifying
    @Transactional
    @Query("delete from Outcome O where O.operation.id =:operationId")
    int deleteByOperation_Id(Long operationId);

    Optional<Outcome> findByOperation_Id(Long operationId);

    @Query(value = "SELECT sum(price*counter) FROM Outcome")
    BigDecimal sumOutcome();
    @Query(value = "SELECT sum(price*counter) FROM Outcome where year =:year")
    BigDecimal sumOutcome(Integer year);
    @Query(value = "SELECT sum(price*counter) FROM Outcome where year =:year AND month =:month")
    BigDecimal sumOutcome(Integer year, Integer month);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.name like :name")
    Stream<Outcome> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.date =:date")
    Stream<Outcome> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.description like :description")
    Stream<Outcome> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.category like :category")
    Stream<Outcome> findAllByCategoryLike(String category);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A " +
            "WHERE I.price =:price")
    Stream<Outcome> findAllByPrice(BigDecimal price);

    @Query(value = "SELECT I FROM Outcome I LEFT JOIN FETCH I.operation O LEFT JOIN FETCH O.account A")
    Stream<Outcome> findTotalAll(Pageable page);
}
