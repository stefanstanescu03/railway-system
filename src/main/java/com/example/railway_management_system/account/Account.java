package com.example.railway_management_system.account;

import com.example.railway_management_system.ticket.Ticket;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    private Long accountId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;

    @OneToMany(mappedBy = "account",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Ticket> ticketsList;

    public Account() {

    }

    public Account(String password, String phone, String email, String lastName, String firstName) {
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public void setTicketsList(List<Ticket> ticketsList) {
        this.ticketsList = ticketsList;
        for (Ticket ticket : ticketsList) {
            ticket.setAccount(this);
        }
    }

    public void addTicket(Ticket ticket) {
        ticket.setAccount(this);
        this.ticketsList.add(ticket);
    }

    public Account(Long accountId, String firstName, String lastName,
                   String email, String phone, String password) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public List<Ticket> getTicketsList() {
        return ticketsList;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
