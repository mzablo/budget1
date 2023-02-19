package mza.thy.filter;

import java.util.Optional;
import java.util.stream.Stream;

public interface FilterDescriptionType<T> {

    Stream<T> findAllByDescriptionLike(String description);
}
