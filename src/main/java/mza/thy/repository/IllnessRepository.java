package mza.thy.repository;

import mza.thy.domain.Illness;
import mza.thy.domain.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.Stream;

@Repository
public interface IllnessRepository extends JpaRepository<Illness, Long> {

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.name like :name")
    Stream<Illness> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.date =:date")
    Stream<Illness> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.description like :description")
    Stream<Illness> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.who like :who")
    Stream<Illness> findAllByWhoLike(String who);
}
