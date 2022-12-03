package mza.thy.repository;

import mza.thy.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByNameLike(String name);

    List<Account> findAllByBankLike(String bank);

    Page<Account> findAll(Pageable pageable);
}
