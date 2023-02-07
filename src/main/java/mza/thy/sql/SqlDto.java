package mza.thy.sql;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class SqlDto {
    String sql;
    String result;
}

