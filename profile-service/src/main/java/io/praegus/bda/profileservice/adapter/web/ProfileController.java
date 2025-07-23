package io.praegus.bda.profileservice.adapter.web;

import io.praegus.bda.profileservice.business.ProfileService;
import org.openapitools.api.ProfilesApi;
import org.openapitools.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProfileController implements ProfilesApi {

    Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @Override
    public ResponseEntity<String> createProfile(Profile profile) {
        profileService.createProfile(profile);
        logger.info("new profile {} created", profile.getUsername());
        return ResponseEntity.ok("Superduper!");
    }

    @Override
    public ResponseEntity<Void> deleteProfile(String username) {
        profileService.deleteProfile(username);
        logger.info("profile {} deleted", username);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteProfiles() {
        profileService.deleteAllProfiles();
        logger.info("all profiles deleted");
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Profile>> getAllProfiles() {
        logger.info("retrieving all profiles");
        return ResponseEntity.ok(profileService.getAllProfiles());
    }

    @Override
    public ResponseEntity<Profile> getSingleProfile(String username) {
        logger.info("retrieving profile {}", username);
        return ResponseEntity.ok(profileService.getProfile(username));
    }

    @Override
    public ResponseEntity<String> updateProfile(String username, Profile profile) {
        profileService.updateProfile(username, profile);
        logger.info("profile {} updated", username);
        return ResponseEntity.ok("Profile updated!");
    }
}
