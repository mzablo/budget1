package mza.thy.filter;

import java.util.stream.Stream;

public interface FilterWhoType<T> {

    Stream<T> findAllByWhoLike(String who);
}
