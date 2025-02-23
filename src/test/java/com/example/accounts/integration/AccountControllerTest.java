package com.example.accounts.integration;

import com.example.accounts.dto.AccountDto;
import com.example.accounts.model.Account;
import com.example.accounts.repository.AccountRepository;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeAll
    static void setUpData(@Autowired ApplicationContext applicationContext) {
        AccountRepository accountRepository = applicationContext.getBean(AccountRepository.class);
        if (accountRepository.count() == 0) {
            List<Account> defaultAccounts = Arrays.asList(
                    new Account("account1", Currency.getInstance("KES"), BigDecimal.valueOf(100000), false),
                    new Account("account2", Currency.getInstance("KES"), BigDecimal.valueOf(0), false),
                    new Account("account3", Currency.getInstance("KES"), BigDecimal.valueOf(1000), true)

            );

            accountRepository.saveAll(defaultAccounts);
        }


    }

    @Test
    void listAccounts() {
        given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .param("page", 0)
                .param("pageSize", 50)
                .when().get("/accounts").then().statusCode(HttpStatus.OK.value()).log().all();
    }

    @Test
    void createAccount_Treasury() {
        AccountDto accountDto = new AccountDto(null, "account_treasury", Currency.getInstance("KES"), BigDecimal.valueOf(1000).negate(),
                true, null, null, null

        );
        given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(accountDto)
                .when().post("/accounts").then().statusCode(HttpStatus.OK.value()).log().all();


    }

    @Test
    void createAccount_non_Treasury() {
        AccountDto accountDto = new AccountDto(null, "account_non_treasury", Currency.getInstance("KES"), BigDecimal.valueOf(1000),
                false, null, null, null

        );
        given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(accountDto)
                .when().post("/accounts").then().statusCode(HttpStatus.OK.value()).log().all();


    }

    @Test
    void transferMoney_fromTreasurytoNonTreasury() {
        AccountDto accountDto = new AccountDto(null, null, null, null,
                null, 3L, 2L, BigDecimal.valueOf(10000)

        );
        given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(accountDto)
                .when().post("/accounts/transfer").then().statusCode(HttpStatus.OK.value()).log().all();


    }

    @Test
    void transferMoney_nonTreasurytoTreasury_insufficient_balance() {
        AccountDto accountDto = new AccountDto(null, null, null, null,
                null, 1L, 2L, BigDecimal.valueOf(1000000)

        );
        Response response = given().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(accountDto)
                .when().post("/accounts/transfer").then().statusCode(HttpStatus.BAD_REQUEST.value()).log().all().extract().response();
    response.then().body("message",equalTo("Insufficient balance for non-treasury account"));

    }

    @Test
    void transferMoney_nonTreasurytoTreasury() {
        AccountDto accountDto = new AccountDto(null, null, null, null,
                null, 1L, 2L, BigDecimal.valueOf(10000)

        );
        Response response = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(accountDto)
                .when()
                .post("/accounts/transfer")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .extract()
                .response();

// Convert response to a String to compare plain text
        String responseBody = response.asString();
        assertEquals("Transfer successful!", responseBody);



    }
}