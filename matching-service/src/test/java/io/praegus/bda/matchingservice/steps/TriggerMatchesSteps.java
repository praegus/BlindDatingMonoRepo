package io.praegus.bda.matchingservice.steps;

import io.cucumber.java.en.When;
import io.praegus.bda.matchingservice.business.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;

public class TriggerMatchesSteps {

    @Autowired
    MatchingService matchingService;

    @When("the matches are calculated")
    public void calculateMatches() {
        matchingService.attemptMatching();
    }
}
