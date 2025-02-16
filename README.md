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

what distinguishes  a treasury account from a non-treasury account is on the request body ensure treasury field is true when account is treasury  "treasury":true, treasure field is true and vice versa when non-treasury is false.

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
here is he structure 

src
test/com/example/perfomance
 AccountTransferSimulation.java

To be able to execute this class one has to use maven commands for the same 
First begin by running To run the test, you first have to start the Spring Boot web application. 
You can do this, for instance, by going to the project base directory and execute the following command: 
mvn spring-boot:run.
Once the application is started, you can run the Gatling simulation by executing
 mvn gatling:test.

 Remember these two commands one has to run them concurrently: start with mvn spring-boot:run then mvn gatling:test in another terminal concurrently



