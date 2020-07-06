package com.aliengame.server.controller;

import com.aliengame.server.entity.Account;
import com.aliengame.server.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("server_program13/api/account")
@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Catches the register request, check whether account exists, then make operation
     *
     * @param account Account object from body of request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> registerAccount(@RequestBody Account account) {
        Account userExists = accountService.findUserByUsername(account.getUsername());
        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username Already Exists");
        } else {
            Account registerAccount = accountService.registerAccount(account);
            if (registerAccount == null) {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Please Provide All Necessary Information");
            }
            return ResponseEntity.status(HttpStatus.OK).body("Account created successfully");
        }
    }

    /**
     * Catches the login requests, check whether account exists, then make operations
     *
     * @param account Account object from body of request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> loginAccount(@RequestBody Account account) {
        Account loggedAccount = accountService.loginAccount(account);
        if (loggedAccount != null) {
            return ResponseEntity.status(HttpStatus.OK).body(String.valueOf(loggedAccount.getId()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username or password are wrong.");
    }

    /**
     * Catches the delete requests of accounts
     *
     * @param id id from path of request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAccount(@PathVariable long id) {
        Account userExists = accountService.findUserByID(id);
        if (userExists != null) {
            accountService.deleteAccount(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfull delete");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    /**
     * Catches the update requests of accounts
     * @param id id from path of request
     * @param account new informations of account with id in param
     * @return ResponseEntity
     */
    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateAccount(@PathVariable long id, @RequestBody Account account) {
        Account userExists = accountService.findUserByID(id);
        if (userExists != null) {
            accountService.updateAccount(account, id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfull update");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    /**
     * Catches the related account get request
     * @param id id of wanted account
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getAccount/{id}", method = RequestMethod.GET)
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        Account userExists = accountService.findUserByID(id);
        if (userExists != null) {
            return ResponseEntity.status(HttpStatus.OK).body(userExists);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    /**
     * Catches the all accounts get request
     * @return ResponseEntity
     */
    @RequestMapping(value = "/getAccounts", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accountList = accountService.getAllAccounts();
        if (accountList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(accountList);
    }

    /**
     * updates status OFFLINE, IN_GAME, LOBBY, WAITING
     * @param userid
     * @param status
     * @return
     */
    @RequestMapping(value = "/status/{userid}/{status}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateAccountStatus(@PathVariable String userid, @PathVariable String status) {
        Account accountExists = accountService.findUserByID(Long.valueOf(userid));
        if (accountExists != null) {
            Account account = accountService.setStatus(accountExists.getUsername(), status);
            if (account != null) {
                return ResponseEntity.status(HttpStatus.OK).body("Successfull Status Set");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed Status Set");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    /**
     * For matching with polling
     * @param id
     * @return
     */
    @RequestMapping(value = "/match/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getMatchAccount(@PathVariable String id) {
        String matchedPlayerUsername = accountService.getMatchedAccountId(id);
        if (matchedPlayerUsername == null || matchedPlayerUsername.length() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("-");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(matchedPlayerUsername);
    }

}
