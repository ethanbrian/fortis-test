🚀 Fortis-Test

📌 Overview

This project is a Spring Boot RESTful API designed for managing accounts and handling money transfers between them. It enables users to:

✅ Create accounts

✅ Retrieve account details (list accounts)

✅ Perform transactions with business rule enforcement

✅ Run performance tests using Gatling

🛠️ Tech Stack

Backend: Spring Boot (Java 17+)

Database: H2 (In-Memory)

Build Tool: Maven

Performance Testing: Gatling

🔧 Setup Instructions

📌 Prerequisites

Ensure you have the following installed:

☕ Java 17+

💻 IDE: IntelliJ IDEA (Recommended)

📦 Maven

🗄️ H2 Database (Runs in-memory for easy local execution)

📌 Clone the Repository

git clone https://github.com/ethanbrian/fortis-test.git
cd fortis-test

📌 Build and Run

mvn clean install
mvn spring-boot:run

📡 API Endpoints

🔹 Account Management

Method

Endpoint

Description

POST

/accounts

Create a new account

GET

/accounts

Retrieve account details

Request Example: Create Account

{
    "name": "account1",
    "currency": "KES",
    "treasury": false,
    "balance": 10000
}

🔹 Money Transfers

Method

Endpoint

Description

POST

/accounts/transfer (from treasury)

Transfer money between accounts

POST

/accounts/transfer (non-treasury)

Transfer money between accounts

Request Example: Treasury Account Transfer

{
    "fromAccountId": 1,
    "toAccountId": 2,
    "amount": 100
}

Request Example: Non-Treasury Account Transfer

{
    "fromAccountId": 3,
    "toAccountId": 2,
    "amount": 10000
}

🔹 Treasury vs. Non-Treasury Accounts:

Treasury Account ➝ "treasury": true

Non-Treasury Account ➝ "treasury": false

🚀 Running Performance Tests (Gatling)

📌 Ensure the following dependencies are available in pom.xml:

<!-- Gatling Core -->
<dependency>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-app</artifactId>
    <version>3.7.2</version>
</dependency>

<!-- Gatling Charts -->
<dependency>
    <groupId>io.gatling.highcharts</groupId>
    <artifactId>gatling-charts-highcharts</artifactId>
    <version>3.7.2</version>
</dependency>

📌 Folder Structure

src/
│── test/com/example/performance/
│   ├── AccountTransferSimulation.java

📌 Running Performance Tests

1️⃣ Start the Spring Boot application

mvn spring-boot:run

2️⃣ Run Gatling Tests in a separate terminal

mvn gatling:test

📢 Ensure both commands run concurrently!

🔄 Data Initialization

To ensure smooth test execution, initial data is preloaded into the database when the application starts.

**Snippet from **``

@Override
public void run(String... args) throws Exception {
    if(accountRepository.count() == 0) {
        List<Account> defaultAccounts = Arrays.asList(
            new Account("account1", Currency.getInstance("KES"), BigDecimal.valueOf(100000), false),
            new Account("account2", Currency.getInstance("KES"), BigDecimal.valueOf(0), false),
            new Account("account3", Currency.getInstance("KES"), BigDecimal.valueOf(1000), true)
        );
        accountRepository.saveAll(defaultAccounts);
    }
}

❗ Exception Handling

More descriptive error messages have been added under the Exception folder.

Example: BadRequestException.java

public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String message = "Bad Request.";
    private List<String> errorList = new ArrayList<>();
    
    public BadRequestException(String message) {
        super(message);
        this.message = message;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}

📜 Pagination for Listing Accounts

To improve performance and usability, pagination has been implemented in the /accounts endpoint.

Snippet from Controller

public Page<Account> listAccounts(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "pageSize", defaultValue = "50") int pageSize) {
    return accountRepository.findAll(PageRequest.of(page, pageSize));
}

📢 Now, users can fetch accounts efficiently using page parameters!

📝 DTO Implementation using Records

Since this object is purely data-centric, Java records are used for DTOs.

public record AccountDto(
    Long id, String name, Currency currency, BigDecimal balance,
    Boolean treasury, Long fromAccountId, Long toAccountId, BigDecimal amount) {}

🎉 Summary

This project provides a robust and performant Spring Boot API with:

🔹 Account Management (Create, List)

🔹 Money Transfers (Treasury & Non-Treasury)

🔹 Performance Testing with Gatling

🔹 Preloaded Test Data for Seamless Execution

🔹 Exception Handling for Better Debugging

🔹 Pagination Support for Efficient Data Fetching

📢 Happy Coding! 🚀
