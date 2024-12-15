package com.example.railway_management_system.account;

import com.example.railway_management_system.ticket.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // debugging purposes this should not exist!
    @GetMapping
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @PostMapping
    public void registerAccount(@RequestBody Account account) {
        accountService.registerAccount(account);
    }

    @DeleteMapping(path = "{accountId}")
    public void deleteAccount(@PathVariable("accountId") Long accountId) {
        accountService.deleteAccount(accountId);
    }

    @PutMapping(
            path = "id={accountId}&first_name={firstName}&last_name={lastName}" +
                    "&email={email}&phone={phone}")
    public void updateAccount(@PathVariable("accountId") Long accountId,
                              @PathVariable("firstName") String firstName,
                              @PathVariable("lastName") String lastName,
                              @PathVariable("email") String email,
                              @PathVariable("phone") String phone) {
         accountService.updateAccount(accountId, firstName, lastName, email, phone);
    }

    @PostMapping(path = "ticket/id={accountId}")
    public void addTicket(@PathVariable("accountId") Long accountId,
                          @RequestBody Ticket ticket) {
        accountService.addTicket(accountId, ticket);
    }
}
