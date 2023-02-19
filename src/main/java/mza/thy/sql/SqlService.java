package mza.thy.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
class SqlService {
    private final EntityManager entityManager;

    //dorobic predefiniowane sql z pliku
    //select i.name from Income i where i.id in (1,2,5,8)
    @Transactional(readOnly = true)
    public SqlDto getResult(SqlDto sql) {
        log.debug("Sql: {}", sql);
        var x = getQuery(sql);
        return SqlDto.builder()
                .sql(sql.getSql())
                .result(x)
                //  .resultList(x.getResultList().stream().toList())
                .build();
    }

    private String getQuery(SqlDto sql) {
        try {
            var x = entityManager.createNativeQuery(sql.getSql()).getResultList();
            log.debug("result {}", x);
            return x.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "error " + e.getMessage();
        }
    }

}
