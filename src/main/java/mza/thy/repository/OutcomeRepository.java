package mza.thy.repository;

import mza.thy.domain.Income;
import mza.thy.domain.Outcome;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

@Repository
public interface OutcomeRepository extends JpaRepository<Outcome, Long> {

    int deleteByOperationId(Long operationId);

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
    Stream<Outcome> findTotalAll(Sort sort);
}
