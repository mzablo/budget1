package mza.thy.repository;

import mza.thy.domain.Deposit;
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
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE D.bank like :name")
    Stream<Deposit> findAllByBankLike(String name);

    @Query(value = "SELECT D FROM Deposit D " +
            "WHERE D.description like :description")
    Stream<Deposit> findAllByDescriptionLike(String description);

    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.date =:date")
    Stream<Deposit> findAllByDate(LocalDate date);

    @Query(value = "SELECT D from Deposit D " +
            "WHERE D.amount =:amount")
    Stream<Deposit> findAllByAmount(BigDecimal amount);

    @Query(value = "SELECT D FROM Deposit D")
    Stream<Deposit> findTotalAll(Pageable page);
}
