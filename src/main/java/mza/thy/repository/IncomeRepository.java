package mza.thy.repository;

import mza.thy.domain.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    //Stream<Income> findAll(Sort sort);
    Stream<Income> findAllByNameLike(String name);

    Stream<Income> findAllByDate(LocalDate date);

    Stream<Income> findAllByDescriptionLike(String name);

    Stream<Income> findAllByAmount(BigDecimal amount);

    Page<Income> findAll(Pageable pageable);
}
