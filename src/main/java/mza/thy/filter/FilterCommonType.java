package mza.thy.filter;

import java.util.Optional;
import java.util.stream.Stream;

public interface FilterCommonType<T> {

    FilterHandler getFilter();
    Optional<T> findAllById(Long id);
}
