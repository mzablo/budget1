package mza.thy.filter;

import java.math.BigDecimal;
import java.util.stream.Stream;

public interface FilterAmountType<T> {

    Stream<T> findAllByAmount(BigDecimal amount);
}
