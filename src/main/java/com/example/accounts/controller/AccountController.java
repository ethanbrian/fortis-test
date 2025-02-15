package com.example.accounts.controller;
import com.example.accounts.dto.AccountDto;
import com.example.accounts.model.Account;
import com.example.accounts.repository.AccountRepository;
import com.example.accounts.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final AccountRepository accountRepository;

    public AccountController(AccountService accountService, AccountRepository accountRepository) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @GetMapping

    public Page<Account> listAccounts(@RequestParam (name="page",defaultValue = "0")int page,
                                      @RequestParam (name="pageSize",defaultValue = "50")int pageSize){
        return accountRepository.findAll(PageRequest.of(page,pageSize));
    }

    @PostMapping
    public Account createAccount(@RequestBody AccountDto payload) {

        return accountService.createAccount(payload);
    }

    @PostMapping("/transfer")
    public String transferMoney(@RequestBody AccountDto payload) {


        accountService.transferMoney(payload.fromAccountId(),payload.toAccountId(),payload.amount());
        return "Transfer successful!";
    }
}

