package com.aliengame.server;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Status;
import com.aliengame.server.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void findByUsername() {

        Account testAccount = Account.builder()
                .name("ekin")
                .surname("pekin")
                .email("ekin@random")
                .password("ekin123")
                .username("ekin123")
                .status(Status.valueOf("IN_LOBBY"))
                .build();

        accountRepository.save(testAccount);

        assertNotNull(accountRepository.findByUsername("ekin123"));

        assertEquals(testAccount, accountRepository.findByUsername("ekin123"));

    }

    @Test
    public void deleteById() {

        Account testAccount1 = Account.builder()
                .name("utku")
                .surname("kutlu")
                .email("utku@random")
                .password("542dsa")
                .username("utku*")
                .status(Status.valueOf("IN_LOBBY"))
                .build();

        accountRepository.save(testAccount1);

        accountRepository.deleteById(testAccount1.getId());

        assertEquals(accountRepository.count(),24);

    }
}
