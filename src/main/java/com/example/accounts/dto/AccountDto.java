package com.example.accounts.dto;

import java.math.BigDecimal;
import java.util.Currency;

public record AccountDto(Long id, String name, Currency currency, BigDecimal balance,
                         Boolean treasury,Long fromAccountId,Long toAccountId,BigDecimal amount) {

}
