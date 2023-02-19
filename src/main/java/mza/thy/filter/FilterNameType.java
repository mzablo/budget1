package mza.thy.filter;

import java.util.Optional;
import java.util.stream.Stream;

public interface FilterNameType<T> {
    Stream<T> findAllByNameLike(String name);
}
