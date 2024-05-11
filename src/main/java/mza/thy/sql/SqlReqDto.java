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
class SqlReqDto {
    String sql;

    public String getSql() {
        return StringUtils.hasLength(sql) ? sql : "select top 100 * from outcome order by id desc";
    }

    boolean isUpdate() {
        return getSql().toUpperCase().contains("UPDATE");
    }
}

