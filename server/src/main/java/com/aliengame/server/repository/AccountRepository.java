package com.aliengame.server.repository;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);

    Account findById(long id);

    void deleteById(long id);

    List<Account> findAllByStatusEquals(Status status);

}
