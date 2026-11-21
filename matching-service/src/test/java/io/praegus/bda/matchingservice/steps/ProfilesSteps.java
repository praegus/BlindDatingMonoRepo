package io.praegus.bda.matchingservice.steps;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import org.openapitools.client.model.Characteristics;
import org.openapitools.client.model.Gender;
import org.openapitools.client.model.HairColor;
import org.openapitools.client.model.MusicGenre;
import org.openapitools.client.model.Profile;
import tools.jackson.databind.json.JsonMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class ProfilesSteps {
    private final List<Profile> profiles = new ArrayList<>();
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Given("profile {string} exists with these characteristics")
    public void profileExists(String username, DataTable table) {
        if (profiles.stream().anyMatch(profile -> profile.getUsername().equals(username))) {
            throw new IllegalArgumentException("Duplicate profile: " + username);
        }
        profiles.add(parseProfile(username, table));
        publishProfiles();
    }

    @After
    public void clearProfiles() {
        profiles.clear();
        publishProfiles();
    }

    private void publishProfiles() {
        stubFor(WireMock.get("/profiles").willReturn(
                WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(jsonMapper.writeValueAsString(profiles))
        ));
    }

    static Profile parseProfile(String username, DataTable table) {
        if (username == null || !username.matches("[a-zA-Z0-9]{1,20}")) {
            throw new IllegalArgumentException("Username must contain 1-20 letters or digits");
        }
        if (table.cells().isEmpty() || !table.row(0).equals(
                List.of("characteristic", "personal", "preference", "dislike"))) {
            throw new IllegalArgumentException(
                    "Expected columns: characteristic | personal | preference | dislike");
        }
        var profile = new Profile();
        profile.setUsername(username);
        profile.setPersonalInformation(new Characteristics());
        profile.setPreferences(new Characteristics());
        profile.setDislikes(new Characteristics());
        var seen = new HashSet<String>();
        for (var row : table.cells().subList(1, table.height())) {
            String characteristic = row.get(0);
            if (characteristic == null || !seen.add(characteristic)) {
                throw new IllegalArgumentException("Missing or duplicate characteristic: " + characteristic);
            }
            setCharacteristic(profile.getPersonalInformation(), characteristic, row.get(1));
            setCharacteristic(profile.getPreferences(), characteristic, row.get(2));
            setCharacteristic(profile.getDislikes(), characteristic, row.get(3));
        }
        return profile;
    }

    private static void setCharacteristic(Characteristics target, String name, String raw) {
        String value = raw == null || raw.isBlank() ? null : raw.trim();
        try {
            switch (name) {
                case "gender" -> target.setGender(value == null ? null : Gender.fromValue(value));
                case "hairColor" -> target.setHairColor(value == null ? null : HairColor.fromValue(value));
                case "pets" -> target.setPets(parseBoolean(value));
                case "tattoos" -> target.setTattoos(parseBoolean(value));
                case "favoriteColor" -> target.setFavoriteColor(value);
                case "sports" -> target.setSports(value);
                case "musicGenres" -> target.setMusicGenres(value == null ? null :
                        Arrays.stream(value.split(",", -1)).map(String::trim).map(MusicGenre::fromValue).toList());
                default -> throw new IllegalArgumentException("Unknown characteristic: " + name);
            }
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Invalid characteristic '" + name + "' with value '" + raw + "': "
                    + exception.getMessage(), exception);
        }
    }

    private static Boolean parseBoolean(String value) {
        if (value == null) return null;
        return switch (value) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new IllegalArgumentException("Expected true, false, or a blank cell");
        };
    }
}
