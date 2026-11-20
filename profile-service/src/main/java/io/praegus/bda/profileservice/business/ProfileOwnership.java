package io.praegus.bda.profileservice.business;

import io.praegus.bda.profileservice.adapter.data.ProfileRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component("profileOwnership")
public class ProfileOwnership {
    private final ProfileRepository profiles;

    public ProfileOwnership(ProfileRepository profiles) {
        this.profiles = profiles;
    }

    public boolean owns(Authentication authentication, String username) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)
                || !"blinddating-frontend".equals(jwt.getClaimAsString("azp"))
                || jwt.getSubject() == null
                || !username.equals(jwt.getClaimAsString("preferred_username"))) {
            return false;
        }
        return profiles.findById(username)
                .map(profile -> jwt.getSubject().equals(profile.getKeycloakSubject()))
                .orElse(false);
    }
}
