package com.example.accounts;

import com.example.accounts.model.Account;
import com.example.accounts.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@SpringBootApplication
public class AccountsApplication implements CommandLineRunner {
private final AccountRepository accountRepository;

    public AccountsApplication(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		if(accountRepository.count()==0){
			List<Account> defaultAccounts = Arrays.asList(
					new Account("account1", Currency.getInstance("KES"), BigDecimal.valueOf(100000), false),
					new Account("account2", Currency.getInstance("KES"), BigDecimal.valueOf(0), false),
					new Account("account3", Currency.getInstance("KES"), BigDecimal.valueOf(1000), true)

			);

			accountRepository.saveAll(defaultAccounts);
}
	}
}
