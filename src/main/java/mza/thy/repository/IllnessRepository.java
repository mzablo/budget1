package mza.thy.repository;

import mza.thy.domain.Illness;
import mza.thy.domain.Outcome;
import mza.thy.filter.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.stream.Stream;

@Repository
public interface IllnessRepository extends JpaRepository<Illness, Long>,
        FilterCommonType<Illness>, FilterNameType<Illness>, FilterDateType<Illness>, FilterDescriptionType<Illness>, FilterWhoType<Illness> {

    @Override
    default FilterHandler<Illness> getFilter() {
        var filter = new FilterHandler<Illness>(this);
        filter.setDateFilter(this);
        filter.setNameFilter(this);
        filter.setDescriptionFilter(this);
        filter.setWhoFilter(this);
        return filter;
    }

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE lower(I.name) like :name order by id desc")
    Stream<Illness> findAllByNameLike(String name);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.date =:date order by id desc")
    Stream<Illness> findAllByDate(LocalDate date);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.year =:year order by id desc")
    Stream<Illness> findAllByYear(int year);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.month =:month order by id desc")
    Stream<Illness> findAllByMonth(int month);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.year =:year and I.month =:month order by id desc")
    Stream<Illness> findAllByYearAndMonth(int year, int month);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE lower(I.description) like :description order by id desc")
    Stream<Illness> findAllByDescriptionLike(String description);

    @Query(value = "SELECT I FROM Illness I " +
            "WHERE I.who like :who order by id desc")
    Stream<Illness> findAllByWhoLike(String who);
}
