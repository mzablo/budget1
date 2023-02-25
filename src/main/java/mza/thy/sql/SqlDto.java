package mza.thy.sql;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Builder
@Value
@ToString
public class SqlDto {
    String sql;
    List<String> headers;
    List<Map<String, Object>> rows;
}

