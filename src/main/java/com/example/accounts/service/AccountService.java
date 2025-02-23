package com.example.accounts.service;


import com.example.accounts.dto.AccountDto;
import com.example.accounts.exception.BadRequestException;
import com.example.accounts.model.Account;
import com.example.accounts.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(AccountDto dto) {
        if (dto.name() == null)
            throw new RuntimeException("Account name is required");
        if (dto.currency() == null)
            throw new RuntimeException("Currency is required");
        if (dto.treasury() == null)
            throw new RuntimeException("Treasury is required");

        BigDecimal balance = dto.balance();
        if(balance == null)
            balance = BigDecimal.ZERO;
        if(balance.doubleValue()<0&& !dto.treasury())
            throw new BadRequestException("Only Treasury Accounts can have negative balances");

        Account account = new Account(dto.name(), dto.currency(),balance, dto.treasury());
        return accountRepository.save(account);
    }

    public void transferMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        if(amount==null || amount.doubleValue()<1)
            throw new BadRequestException("Enter Valid Amount");

        Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Sender account not found"));
        Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new EntityNotFoundException("Receiver account not found"));

        if (!fromAccount.isTreasury() && fromAccount.getBalance().compareTo(amount) < 0) {
            throw new BadRequestException("Insufficient balance for non-treasury account");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}

