package mza.thy.sql;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.util.List;

@Builder
@Value
@ToString
public class SqlDto {
    String sql;
    String result;
    List<String> resultList;

    public String getSql() {
        return StringUtils.hasLength(sql) ? sql : "select i.name from Income i where i.id in (1,2,5,8)";
    }

    boolean isSelect() {
        return sql.toUpperCase().contains("SELECT");
    }
}

