package com.example.railway_management_system.account;

import com.example.railway_management_system.ticket.Ticket;
import com.example.railway_management_system.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TicketRepository ticketRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          TicketRepository ticketRepository) {
        this.accountRepository = accountRepository;
        this.ticketRepository = ticketRepository;
    }

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public void registerAccount(Account account) {
        Optional<Account> accountByEmail = accountRepository
                .findAccountByEmail(account.getEmail());
        if (accountByEmail.isPresent()) {
            throw new IllegalStateException("email taken");
        }

        if (Objects.equals(account.getFirstName(), null)) {
            throw new IllegalStateException("first_name empty");
        }

        if (Objects.equals(account.getLastName(), null)) {
            throw new IllegalStateException("last_name empty");
        }

        if (Objects.equals(account.getEmail(), null)) {
            throw new IllegalStateException("email empty");
        }

        if (Objects.equals(account.getPhone(), null)) {
            throw new IllegalStateException("phone empty");
        }

        if (Objects.equals(account.getPassword(), null)) {
            throw new IllegalStateException("password empty");
        }

        if (invalidEmail(account.getEmail())) {
            throw new IllegalStateException("email is not valid");
        }

        if (invalidPhone(account.getPhone())) {
            throw new IllegalStateException("phone is not valid");
        }

        if (!validatePassword(account.getPassword())) {
            throw new IllegalStateException("password is too simple");
        }

        accountRepository.save(account);
    }

    public void deleteAccount(Long accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id " + accountId + " does not exists");
        }
        accountRepository.deleteById(accountId);
    }

    public void updateAccount(Long accountId, String firstName, String lastName,
                              String email, String phone) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id " + accountId + " does not exists");
        }
        Account accountToUpdate = accountRepository.getReferenceById(accountId);

        if (firstName != null && !firstName.isEmpty()) {
            accountToUpdate.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            accountToUpdate.setLastName(lastName);
        }
        if (email != null && !email.isEmpty()) {
            if (invalidEmail(email)) {
                throw new IllegalStateException("email is not valid");
            }
            accountToUpdate.setEmail(email);
        }
        if (phone != null && !phone.isEmpty()) {
            if (invalidPhone(phone)) {
                throw new IllegalStateException("phone is not valid");
            }
            accountToUpdate.setPhone(phone);
        }
        accountRepository.save(accountToUpdate);
    }

    public void addTicket(Long accountId, Ticket ticket) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) {
            throw new IllegalStateException("account with id " + accountId + " does not exists");
        }
        Account account = accountRepository.getReferenceById(accountId);

        account.addTicket(ticket);
        ticketRepository.save(ticket);

    }

    private boolean invalidEmail(String email) {
        if (email == null) {
            return true;
        }
        int at = email.indexOf("@");
        if (at < 0) {
            return true;
        }
        int dot = email.lastIndexOf(".");
        return at >= dot;
    }

    private boolean invalidPhone(String phone) {
        if (phone.length() != 10) {
            return true;
        }
        return !phone.startsWith("07");
    }

    private boolean validatePassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int numUpper = 0;
        int numLower = 0;
        int numDigits = 0;
        int numSpecial = 0;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLowerCase(c)) {
                numLower = 1;
            } else
            if (Character.isUpperCase(c)) {
                numUpper = 1;
            } else
            if (Character.isDigit(c)) {
                numDigits = 1;
            } else {
                numSpecial = 1;
            }
        }

        System.out.println(numUpper + numLower + numDigits);

        return numUpper != 0 && numLower != 0 && numDigits != 0 && numSpecial != 0;
    }
}
