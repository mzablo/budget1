package mza.thy.filter;

import java.util.stream.Stream;

public interface FilterCategoryType<T> {

    Stream<T> findAllByCategoryLike(String category);
}
