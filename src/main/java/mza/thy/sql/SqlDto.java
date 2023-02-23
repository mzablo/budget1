package mza.thy.sql;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Builder
@Value
@ToString
public class SqlDto {
    String sql;
    String defaultSql;
    String result;
    List<String> headers;
    List<String> resultList;
    List<Map<String,Object>> rows;
//    List<List<String>> rows;

    public String getSql() {
        return StringUtils.hasLength(sql) ? sql : "select i.name from Income i where i.id > 1000";
    }

    boolean isSelect() {
        return sql.toUpperCase().contains("SELECT");
    }
}

