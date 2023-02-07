package mza.thy.sql;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
@Slf4j
public class SqlService {
    private final EntityManager entityManager;
//dorobic predefiniowane sql z pliku
    //select i.name from Income i where i.id in (1,2,5,8)
    @Transactional(readOnly = true)
    public SqlDto getResult(SqlDto sql) {
        log.debug("Sql: {}", sql);
        var result = "empty";
        if (StringUtils.hasLength(sql.getSql())) {
            var x = entityManager.createQuery(sql.getSql()).getResultList();
            log.debug("result {}", x);
            result = x.toString();
        }
        return new SqlDto(sql.getSql(), result);
    }
}
