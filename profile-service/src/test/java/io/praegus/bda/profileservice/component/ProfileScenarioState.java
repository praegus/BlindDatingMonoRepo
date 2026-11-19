package io.praegus.bda.profileservice.component;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
@ScenarioScope
public class ProfileScenarioState {

    private String username;
    private Response latestResponse;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Response getLatestResponse() {
        return latestResponse;
    }

    public void setLatestResponse(Response latestResponse) {
        this.latestResponse = latestResponse;
    }
}
