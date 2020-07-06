package com.aliengame.server.service;

import com.aliengame.server.entity.Account;
import com.aliengame.server.entity.Role;
import com.aliengame.server.entity.RoleType;
import com.aliengame.server.entity.Status;
import com.aliengame.server.repository.AccountRepository;
import com.aliengame.server.repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final RoleRepository roleRepository;

    public AccountService(AccountRepository accountRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Saves user to database
     *
     * @param account that want to save to DB
     * @return Saved account object
     */
    public Account registerAccount(Account account) {
        try {
            Role userRole;
            userRole = roleRepository.findByRoleType(RoleType.USER);
            account.setRole(userRole);
            account.setPassword(BCrypt.hashpw(account.getPassword(), BCrypt.gensalt()));
            return accountRepository.save(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks account exist then gives OK to login if credentials are OK
     *
     * @param account that want to login
     * @return logged in account
     */
    public Account loginAccount(Account account) {
        try {
            Account temp = accountRepository.findByUsername(account.getUsername());
            if (temp != null) {
                if (BCrypt.checkpw(account.getPassword(), temp.getPassword())) {
                    temp.setStatus(Status.valueOf("IN_LOBBY"));
                    accountRepository.save(temp);
                    return temp;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to update account information
     *
     * @param account updated account information
     * @param id      to update account
     * @return operation result; true for success, false for fail
     */
    public Boolean updateAccount(Account account, long id) {
        Optional<Account> userOptional = Optional.ofNullable(accountRepository.findById(id));
        if (userOptional.isPresent()) {
            account.setId(id);
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    /**
     * Deletes the given account
     * @param id account id to be deleted
     */
    public void deleteAccount(long id) {
        accountRepository.deleteById(id);
    }

    /**
     * Brings account by username
     * @param username username of searching account
     * @return found account
     */
    public Account findUserByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    /**
     * Brings account by id
     * @param id username of searching account
     * @return found account
     */
    public Account findUserByID(long id) {
        return accountRepository.findById(id);
    }

    /**
     * Set users status to given status
     *
     * @param username
     * @param status
     * @return
     */
    public Account setStatus(String username, String status) {
        Account byUsername = accountRepository.findByUsername(username);
        try {
            byUsername.setStatus(Status.valueOf(status));
            return accountRepository.save(byUsername);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * brings the 1st available player with status WAITING with polling
     * @param id
     * @return
     */
    public String getMatchedAccountId(String id) {
        Account byId = accountRepository.findById(Long.parseLong(id));
        List<Account> in_lobby = accountRepository.findAllByStatusEquals(Status.valueOf("WAITING"));
        in_lobby.remove(byId);
        if (in_lobby.size() != 0) {
            int random_index = new Random().nextInt(in_lobby.size());
            return String.valueOf(in_lobby.get(random_index).getId());
        }
        return "";
    }
}
