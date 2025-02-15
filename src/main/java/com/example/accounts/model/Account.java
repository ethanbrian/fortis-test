package com.example.accounts.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Currency;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Currency currency;
    private BigDecimal balance;
    private boolean treasury;

    public Account() {
    }

    public Account(String name, Currency currency, BigDecimal balance, boolean treasury) {
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.treasury = treasury;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isTreasury() {
        return treasury;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTreasury(boolean treasury) {
        this.treasury = treasury;
    }
}

