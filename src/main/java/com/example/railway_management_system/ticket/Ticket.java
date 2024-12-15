package com.example.railway_management_system.ticket;

import com.example.railway_management_system.account.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table
public class Ticket {
    @Id
    @SequenceGenerator(
            name = "ticket_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long ticketId;

    private Integer seat;
    private Integer car; 
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Ticket() {

    }

    public Ticket(Long ticketId, Integer price, Integer car, Integer seat) {
        this.ticketId = ticketId;
        this.price = price;
        this.car = car;
        this.seat = seat;
    }

    public Ticket(Integer price, Integer car, Integer seat) {
        this.price = price;
        this.car = car;
        this.seat = seat;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getCar() {
        return car;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setCar(Integer car) {
        this.car = car;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", seat=" + seat +
                ", car=" + car +
                ", price=" + price +
                '}';
    }
}
