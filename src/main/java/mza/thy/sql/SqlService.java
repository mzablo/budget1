package mza.thy.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
class SqlService {
    private final EntityManager entityManager;
    private final List<String> sqlList;

    public SqlService(EntityManager entityManager) {
        this.entityManager = entityManager;
        sqlList = prepareSqlList();
    }

    List<String> getSqlList() {
        return sqlList;
    }

    private List<String> prepareSqlList() {
        try {
            return Files.readAllLines(Paths.get("sql.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return List.of("empty");
    }

    @Transactional(readOnly = true)
    public SqlDto getResult(SqlReqDto sql) {
        log.debug("Sql: {}", sql);
        var x = getQuery(sql.getSql());
        var resultList = entityManager.createNativeQuery(sql.getSql()).getResultList();
        try {
            var cos = ((org.hibernate.engine.spi.SessionImplementor) entityManager.getDelegate()).connection().getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return SqlDto.builder()
                .sql(sql.getSql())
                .headers(List.of("h1"))
                .rows(getBetterResult(resultList))
                //.rows(List.of(List.of(x)))
                .result(x)
                //  .resultList(x.getResultList().stream().toList())
                .build();
    }

    private String getQuery(String sql) {
        try {
            var resultList = entityManager.createNativeQuery(sql).getResultList();
            log.debug("result {}", resultList.size());
            return resultList.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error " + e.getMessage();
        }
    }
    private List<Map<String,Object>> getBetterResult(List result) {
        try {
            log.debug("result better {}", result.size());
            return (List<Map<String, Object>>) result.stream()
                    .map(r->Map.of("h1", result.toString()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of(Map.of("a","b"));
        }
    }
}
