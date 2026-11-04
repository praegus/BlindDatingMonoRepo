package io.praegus.bda.profileservice.component;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileUpdateSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private ProfileScenarioState state;

    @Given("I create a profile for user {string}")
    public void createProfileForUser(String username) {
        state.setUsername(username);
        state.setLatestResponse(given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(Map.of("username", username))
                .post("/profiles"));

        assertThat(state.getLatestResponse().statusCode()).isEqualTo(201);
    }

    @When("I update the profile with a street address with {int} characters")
    public void updateProfileWithLongStreetAddress(int length) {
        var street = "a".repeat(length);
        var characteristics = Map.of(
                "gender", "MAN",
                "favoriteColor", "",
                "pets", false,
                "hairColor", "BROWN",
                "tattoos", false,
                "sports", "",
                "musicGenres", List.of("DANCE"));
        var profile = Map.of(
                "username", state.getUsername(),
                "firstname", "Fin",
                "lastname", "Kingma",
                "additionalInfo", "",
                "address", Map.of(
                        "street", street,
                        "streetNumber", "1",
                        "postalCode", "1234AA",
                        "city", "Amsterdam"),
                "personalInformation", characteristics,
                "preferences", characteristics,
                "dislikes", characteristics);

        state.setLatestResponse(given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(profile)
                .put("/profiles/{username}", state.getUsername()));
    }

    @Then("the response status should be {int}")
    public void responseStatusShouldBe(int expectedStatus) {
        assertThat(state.getLatestResponse().statusCode()).isEqualTo(expectedStatus);
    }
}
