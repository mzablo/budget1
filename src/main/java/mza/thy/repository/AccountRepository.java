package mza.thy.repository;

import mza.thy.domain.Account;
import mza.thy.filter.FilterBankNameType;
import mza.thy.filter.FilterCommonType;
import mza.thy.filter.FilterHandler;
import mza.thy.filter.FilterNameType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>,
        FilterCommonType<Account>, FilterNameType<Account>, FilterBankNameType<Account> {

    @Override
    default FilterHandler<Account> getFilter() {
        var filter = new FilterHandler<Account>(this);
        filter.setBankFilter(this);
        filter.setNameFilter(this);
        return filter;
    }

    Stream<Account> findAllByNameLike(String name);

    Stream<Account> findAllByBankLike(String bank);

    Page<Account> findAll(Pageable pageable);

    @Query(value = "SELECT SUM(D.amount) FROM Deposit D " +
            "WHERE ACTIVE is true AND DESCRIPTION NOT LIKE '%ADB%'")
    BigDecimal sumActiveDeposit();
}
