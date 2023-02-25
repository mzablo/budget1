package mza.thy.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
class SqlService {
    private final EntityManager entityManager;
    private final String sqls;
    private String error;

    public SqlService(EntityManager entityManager) {
        this.entityManager = entityManager;
        sqls = prepareSqls();
    }

    String getSqls() {
        return sqls;
    }

    String getError() {
        return error;
    }

    private List<String> prepareSqlList() {
        try {
            return Files.readAllLines(Paths.get("sql.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of("empty");
    }

    private String prepareSqls() {
        try {
            return new String(Files.readAllBytes(Paths.get("sql.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "empty";
    }

    @Transactional
    public SqlDto getResult(SqlReqDto sql) {
        error = null;
        try {
            log.debug("Sql: {}", sql);
            var statement = ((org.hibernate.engine.spi.SessionImplementor) entityManager.getDelegate()).connection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            return sql.isSelect() ? processSelect(statement, sql.getSql()) : processUpdate(statement, sql.getSql());
        } catch (SQLException e) {
            e.printStackTrace();
            error = e.getMessage();
        }
        return SqlDto.builder().build();
    }

    private SqlDto processUpdate(Statement st, String sql) throws SQLException {
        var counter = st.executeUpdate(sql);
        st.close();
        return SqlDto.builder()
                .sql(sql)
                .headers(List.of("Update"))
                .rows(List.of(Map.of("Update", counter)))
                .build();
    }

    private SqlDto processSelect(Statement st, String sql) throws SQLException {
        var rs = st.executeQuery(sql);
        var headers = buildHeaders(rs);
        st.close();
        var result = SqlDto.builder()
                .sql(sql)
                .headers(headers)
                .rows(getRows(rs, headers))
                .build();
        rs.close();
        return result;
    }

    private List<String> buildHeaders(ResultSet rs) throws SQLException {
        var meta = rs.getMetaData();
        var result = new ArrayList<String>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            result.add(meta.getColumnName(i));
        }
        return result;
    }

    private Map<String, Object> createRow(ResultSet rs, List<String> headers) {
        return headers.stream()
                .collect(Collectors.toMap(Function.identity(), h -> getObjectFromRs(rs, h)));
    }

    private Object getObjectFromRs(ResultSet rs, String h) {
        try {
            return Optional.ofNullable(rs.getObject(h)).orElse("");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<Map<String, Object>> getRows(ResultSet rs, List<String> headers) {
        var result = new ArrayList<Map<String, Object>>();
        try {
            if (!rs.first()) {
                return Collections.emptyList();
            }
            do {
                result.add(createRow(rs, headers));
            } while (rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
