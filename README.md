# fortis-test

Overview

This project is a Spring Boot RESTful API for managing accounts and handling money transfers between them. The API allows users to create accounts, retrieve account details (or listing accounts), and perform transactions while enforcing business rules. Performance testing is integrated using Gatling.

Tech Stack

Backend: Spring Boot (Java 17+)
Database: H2 / PostgreSQL
Build Tool: Maven
Performance Testing: Gatling

Setup Instructions

Prerequisites

Java 17+
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
{
    "name":"account1",
    "currency":"KES",
    "treasury":false,
    "balance":10000
}

GET

/accounts

Retrieve account details

POST

/transfers  (from treasury accounts)

Transfer money between accounts

{
    "fromAccountId":1,
    "toAccountId":2,
    "amount":100

}

/transfers  (from non-treasury accounts)

Transfer money between accounts

{
    "fromAccountId":3,
    "toAccountId":2,
    "amount":10000

}

what distinguishes  a treasury account from a non-treasury account is on the request body ensure  "treasury":false, treasure field is true and vice versa when non-treasury is false.


