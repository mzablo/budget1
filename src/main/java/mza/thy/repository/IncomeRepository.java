package mza.thy.repository;

import mza.thy.domain.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findAll(Sort sort);

    List<Income> findAll();

    List<Income> findAllByNameLike(String name);

    List<Income> findAllByDescriptionLike(String name);

    List<Income> findAllByAmount(BigDecimal amount);

    Page<Income> findAll(Pageable pageable);
}
