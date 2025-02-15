package com.example.accounts.perfomance;


import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class AccountTransferSimulation extends Simulation {

    // Define HTTP Protocol Configuration
    private HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .header("Content-Type", "application/json");

    // Define Transfer Request Body
    private String transferBody = """
            {
                "fromAccountId":1,
                "toAccountId":2,
                "amount":100
            }
            """;

    // Define Test Scenario
    private ScenarioBuilder transferScenario = scenario("Transfer Money Load Test")
            .exec(
                    http("Transfer Money")
                            .post("/accounts/transfer")
                            .body(StringBody(transferBody))
                            .asJson()
                            .check(status().is(200))
            );

    // Define Load Simulation
    {
        setUp(
                transferScenario.injectOpen(
                        rampUsers(10).during(Duration.ofMinutes(1)),  // 1 min ramp-up
                        constantUsersPerSec(10).during(Duration.ofMinutes(9)) // 9 min constant load at 10 transactions/min
                )
        ).protocols(httpProtocol);
    }
}






