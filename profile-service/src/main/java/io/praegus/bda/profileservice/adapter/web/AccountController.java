package io.praegus.bda.profileservice.adapter.web;

import io.praegus.bda.profileservice.business.ProfileService;
import org.openapitools.model.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    private final ProfileService profiles;

    public AccountController(ProfileService profiles) {
        this.profiles = profiles;
    }

    @PutMapping("/account/profile")
    @PreAuthorize("principal.claims['azp'] == 'blinddating-frontend'")
    public Profile ensureProfile(@AuthenticationPrincipal Jwt jwt) {
        return profiles.ensureAccountProfile(jwt.getSubject(), jwt.getClaimAsString("preferred_username"));
    }
}
