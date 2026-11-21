package io.praegus.bda.matchingservice.steps;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import org.checkerframework.checker.units.qual.C;
import org.openapitools.client.model.Characteristics;
import org.openapitools.client.model.Profile;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class ProfilesSteps {

    @DataTableType
    public Profile transform(Map<String, String> entry) {
        var profile = new Profile();
        profile.setUsername(entry.get("username"));

        var personal = new Characteristics();
        profile.setPersonalInformation(personal);

        var preferences = new Characteristics();
        profile.setPreferences(preferences);

        var dislikes = new Characteristics();
        profile.setDislikes(dislikes);

        return profile;
    }

    JsonMapper jsonMapper = JsonMapper.builder().build();

    @Given("these profiles exist")
    public void profilesExist(List<Profile> profiles) {
        stubFor(WireMock.get("/profiles").willReturn(
                WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonMapper.writeValueAsString(profiles))
        ));
    }
}
