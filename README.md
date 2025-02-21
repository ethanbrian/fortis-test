# fortis-test

Overview

This project is a Spring Boot RESTful API for managing accounts and handling money transfers between them. The API allows users to create accounts, retrieve account details (or listing accounts), and perform transactions while enforcing business rules. Performance testing is integrated using Gatling.

Tech Stack

Backend: Spring Boot (Java 17+)
Database: H2
Build Tool: Maven
Performance Testing: Gatling

Setup Instructions

Prerequisites

Java 17+
IDE: IntelliJ IDEA
Maven
h2 in memory database i.e easy to run locally

Clone the Repository

git clone https://github.com/ethanbrian/fortis-test.git
cd fortis-test

Build and Run
mvn clean install
mvn spring-boot:run

API Endpoints

Method

Endpoint

Description

POST

/accounts

Create a new account
```bash
{
    "name":"account1",
    "currency":"KES",
    "treasury":false,
    "balance":10000
}
```

GET

/accounts

Retrieve account details

POST

/accounts/transfer  (from treasury accounts)

Transfer money between accounts

```bash
{
    "fromAccountId":1,
    "toAccountId":2,
    "amount":100

}
```

/accounts/transfer (from non-treasury accounts)

Transfer money between accounts
```bash

{
    "fromAccountId":3,
    "toAccountId":2,
    "amount":10000

}
```

what distinguishes  a treasury account from a non-treasury account is on the request body ensure treasury field is true when account is treasury  "treasury":true, and ensure treasure field is false when account is  non-treasury "treasury":false .

To run perfomance tests using Gatling ensure these dependencies are available 

		<!-- Gatling Core -->
		<dependency>
			<groupId>io.gatling</groupId>
			<artifactId>gatling-app</artifactId>
			<version>3.7.2</version>
		</dependency>


		<dependency>
			<groupId>io.gatling.highcharts</groupId>
			<artifactId>gatling-charts-highcharts</artifactId>
			<version>3.7.2</version>
		</dependency>

To run the test file which is located in the test folder under the perfomance folder
here is the structure 

src
test/com/example/perfomance
 AccountTransferSimulation.java

To be able to execute this class one has to use maven commands
one  first has to start the Spring Boot web application.
go to the project base directory and execute the following command: 
```bash
mvn spring-boot:run
```
Once the application is started, you can run the Gatling simulation by executing
```bash
mvn  gatling:test
```
.

Remember these two commands one has to run them concurrently: start with mvn spring-boot:run then mvn gatling:test in another terminal concurrently

To ensure that the perfomance tests run perfectly without erroneous output I have added a code upon starting the application that loads data into the accounts . Here is the snippet 

```bash
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
```


Also added better handling mechanisms under the Exception folder  to give more descriptive error messages 
 here is a sample code for BadRequestException 
```bash
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String message = "Bad Request.";


    private List<String> errorList = new ArrayList<>();

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }

    public BadRequestException(String message, List<String> errorList) {
        super(message);
        this.message = message;
        this.errorList = errorList;
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public List<String> getErrorList() {
        return errorList;
    }
}
```

Another important feature I added is pagination on the Endpoint for listing accounts 
 Here is the snippet 
 

     public Page<Account> listAccounts(@RequestParam (name="page",defaultValue = "0")int page,
                                      @RequestParam (name="pageSize",defaultValue = "50")int pageSize){
        return accountRepository.findAll(PageRequest.of(page,pageSize));
    }
    

    Assuming one has created multiple accounts it would be easy to view the acounts from the Backend.

    Because this object is purely data centric I used records in the dto 

    
    
    public record AccountDto(Long id, String name, Currency currency, BigDecimal balance,
                         Boolean treasury,Long fromAccountId,Long toAccountId,BigDecimal amount) {

}




    That is the summary of the  project !! Happy Coding !!
