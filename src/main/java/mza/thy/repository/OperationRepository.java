package mza.thy.repository;

import mza.thy.domain.Income;
import mza.thy.domain.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;
//!!! zrobic graf aby pozenic pageable i fetch aby nie bylo n+1
@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    @Query(value = "SELECT O FROM Operation O LEFT JOIN FETCH O.account A " +
            "WHERE O.name like :name")
    Stream<Operation> findAllByNameLike(String name);

    Stream<Operation> findAllByDescriptionLike(String description);

    Stream<Operation> findAllByDate(LocalDate date);

    Stream<Operation> findAllByAmount(BigDecimal amount);

    boolean existsByAccountId(Long accountId);
    //@Query(value = "SELECT O FROM Operation O LEFT JOIN FETCH O.account A")
    Page<Operation> findAll(Pageable pageable);

  //  Page<Operation> findAll(Pageable pageable);
}
