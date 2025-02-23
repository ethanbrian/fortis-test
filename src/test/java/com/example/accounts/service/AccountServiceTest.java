package com.example.accounts.service;

import com.example.accounts.dto.AccountDto;
import com.example.accounts.exception.BadRequestException;
import com.example.accounts.model.Account;
import com.example.accounts.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

    class AccountServiceTest {

        @Mock
        private AccountRepository accountRepository;

        @InjectMocks
        private AccountService accountService;


        @Test
        void shouldCreateAccountSuccessfully() {
            // Given
            AccountDto dto = new AccountDto(null, "Test Account", Currency.getInstance("USD"),
                    BigDecimal.valueOf(10000), false, null, null, null);
            Account account = new Account(dto.name(), dto.currency(), dto.balance(), dto.treasury());

            when(accountRepository.save(any(Account.class))).thenReturn(account);

            // When
            Account result = accountService.createAccount(dto);

            // Then
            assertNotNull(result);
            assertEquals("Test Account", result.getName());
            assertEquals(dto.balance(), result.getBalance());
            assertFalse(result.isTreasury());

            verify(accountRepository, times(1)).save(any(Account.class));
        }

        @Test
        void shouldThrowExceptionWhenNameIsNull() {
            // Given
            AccountDto dto = new AccountDto(1L, null, Currency.getInstance("USD"), BigDecimal.TEN, false, null, null, null);

            // When & Then
            Exception exception = assertThrows(RuntimeException.class, () -> accountService.createAccount(dto));
            assertEquals("Account name is required", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenCurrencyIsNull() {
            // Given
            AccountDto dto = new AccountDto(1L, "Test Account", null, BigDecimal.TEN, false, null, null, null);

            // When & Then
            Exception exception = assertThrows(RuntimeException.class, () -> accountService.createAccount(dto));
            assertEquals("Currency is required", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenTreasuryIsNull() {
            // Given
            AccountDto dto = new AccountDto(1L, "Test Account", Currency.getInstance("USD"), BigDecimal.TEN, null, null, null, null);

            // When & Then
            Exception exception = assertThrows(RuntimeException.class, () -> accountService.createAccount(dto));
            assertEquals("Treasury is required", exception.getMessage());
        }

        @Test
        void shouldThrowExceptionWhenNonTreasuryAccountHasNegativeBalance() {
            // Given
            AccountDto dto = new AccountDto(1L, "Test Account", Currency.getInstance("USD"),
                    new BigDecimal("-10"), false, null, null, null);

            // When & Then
            Exception exception = assertThrows(BadRequestException.class, () -> accountService.createAccount(dto));
            assertEquals("Only Treasury Accounts can have negative balances", exception.getMessage());
        }

}