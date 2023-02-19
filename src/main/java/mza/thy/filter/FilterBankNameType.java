package mza.thy.filter;

import java.util.stream.Stream;

public interface FilterBankNameType<T> {

    Stream<T> findAllByBankLike(String category);
}
