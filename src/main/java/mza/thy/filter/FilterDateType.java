package mza.thy.filter;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface FilterDateType<T> {

    Stream<T> findAllByDate(LocalDate date);

    Stream<T> findAllByMonth(int month);

    Stream<T> findAllByYear(int year);

    Stream<T> findAllByYearAndMonth(int year, int month);
}
