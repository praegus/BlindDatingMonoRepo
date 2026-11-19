package io.praegus.bda.profileservice;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Real security chain, JWT validation, services, PostgreSQL and Flyway; only identity-provider HTTP is stubbed. */
@AutoConfigureMockMvc
class ProfileAuthenticationTest extends SupportsPostgresIntegrationTests {
    private static final String ISSUER = "https://identity.example.test/realms/blinddating";
    private static final RSAKey SIGNING_KEY = signingKey();

    @RegisterExtension
    static final WireMockExtension IDENTITY = WireMockExtension.newInstance()
            .options(com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void identityProperties(DynamicPropertyRegistry properties) {
        properties.add("spring.security.oauth2.resourceserver.jwt.issuer-uri", () -> ISSUER);
        properties.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", () -> IDENTITY.baseUrl() + "/certs");
    }

    @Autowired MockMvc mvc;

    @org.junit.jupiter.api.BeforeEach
    void publicKeys() {
        IDENTITY.stubFor(com.github.tomakehurst.wiremock.client.WireMock.get(urlEqualTo("/certs")).willReturn(okJson(
                "{\"keys\":[" + SIGNING_KEY.toPublicJWK().toJSONString() + "]}")));
    }

    @Test
    void rejectsMissingAndInvalidTokens() throws Exception {
        // OpenSpec: authentication.validate-tokens
        mvc.perform(put("/account/profile")).andExpect(status().isUnauthorized());
        mvc.perform(put("/account/profile").header("Authorization", "Bearer malformed"))
                .andExpect(status().isUnauthorized());
        for (String token : new String[] {
                token("alice", "subject", "blinddating-frontend", "", "wrong-issuer", "profile-service", Instant.now().plusSeconds(300), SIGNING_KEY),
                token("alice", "subject", "blinddating-frontend", "", ISSUER, "wrong-audience", Instant.now().plusSeconds(300), SIGNING_KEY),
                token("alice", "subject", "blinddating-frontend", "", ISSUER, "profile-service", Instant.now().minusSeconds(120), SIGNING_KEY),
                token("alice", "subject", "blinddating-frontend", "", ISSUER, "profile-service", Instant.now().plusSeconds(300), signingKey())
        }) {
            mvc.perform(put("/account/profile").header("Authorization", "Bearer " + token))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Test
    void provisionsOnceAndAllowsOwnerToReadAndUpdate() throws Exception {
        // OpenSpec: authentication.own-profile
        String username = username();
        String token = user(username, UUID.randomUUID().toString());
        for (int i = 0; i < 2; i++) {
            mvc.perform(put("/account/profile").header("Authorization", token))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.username").value(username));
        }
        mvc.perform(put("/profiles/" + username).header("Authorization", token)
                .contentType("application/json").content("{\"username\":\"" + username + "\",\"firstname\":\"Alice\"}"))
                .andExpect(status().isOk());
        mvc.perform(get("/profiles/" + username).header("Authorization", token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.firstname").value("Alice"));
    }

    @Test
    void rejectsOtherUsersAndMismatchedBody() throws Exception {
        // OpenSpec: authentication.isolate-profiles
        String username = username();
        String owner = user(username, UUID.randomUUID().toString());
        mvc.perform(put("/account/profile").header("Authorization", owner)).andExpect(status().isOk());
        String attacker = user(username(), UUID.randomUUID().toString());
        mvc.perform(get("/profiles/" + username).header("Authorization", attacker)).andExpect(status().isOk());
        mvc.perform(delete("/profiles/" + username).header("Authorization", attacker)).andExpect(status().isForbidden());
        mvc.perform(put("/profiles/" + username).header("Authorization", attacker)
                .contentType("application/json").content("{\"username\":\"" + username + "\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(put("/profiles/" + username).header("Authorization", owner)
                .contentType("application/json").content("{\"username\":\"someoneelse\"}"))
                .andExpect(status().isForbidden());
        mvc.perform(get("/profiles").header("Authorization", owner)).andExpect(status().isOk());
        mvc.perform(delete("/profiles").header("Authorization", owner)).andExpect(status().isForbidden());
        mvc.perform(post("/profiles").header("Authorization", owner)
                .contentType("application/json").content("{\"username\":\"someoneelse\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void doesNotAllowReusedUsernameToClaimAnExistingProfile() throws Exception {
        // OpenSpec: authentication.isolate-profiles
        String username = username();
        mvc.perform(put("/account/profile").header("Authorization", user(username, UUID.randomUUID().toString())))
                .andExpect(status().isOk());
        String newAccount = user(username, UUID.randomUUID().toString());
        mvc.perform(put("/account/profile").header("Authorization", newAccount)).andExpect(status().isForbidden());
        mvc.perform(delete("/profiles/" + username).header("Authorization", newAccount)).andExpect(status().isForbidden());
    }

    @Test
    void publicReadsDoNotAllowAnonymousWrites() throws Exception {
        // OpenSpec: authentication.public-service-reads
        String username = username();
        mvc.perform(put("/account/profile").header("Authorization", user(username, UUID.randomUUID().toString())))
                .andExpect(status().isOk());
        mvc.perform(get("/profiles")).andExpect(status().isOk());
        mvc.perform(get("/profiles/" + username)).andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
        mvc.perform(get("/profiles/" + username())).andExpect(status().isNotFound());
        mvc.perform(put("/account/profile")).andExpect(status().isUnauthorized());
        mvc.perform(put("/profiles/" + username)
                .contentType("application/json").content("{\"username\":\"" + username + "\"}"))
                .andExpect(status().isUnauthorized());
        mvc.perform(delete("/profiles/" + username)).andExpect(status().isUnauthorized());
    }

    @Test
    void allowsOnlyConfiguredBrowserOrigins() throws Exception {
        // OpenSpec: authentication.browser-origins
        mvc.perform(options("/account/profile").header("Origin", "http://localhost:4200")
                .header("Access-Control-Request-Method", "PUT")
                .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk()).andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
        mvc.perform(options("/account/profile").header("Origin", "https://untrusted.example")
                .header("Access-Control-Request-Method", "PUT")).andExpect(status().isForbidden());
    }

    private static String username() { return "u" + UUID.randomUUID().toString().replace("-", "").substring(0, 18); }

    private static RSAKey signingKey() {
        try { return new RSAKeyGenerator(2048).keyID("test-key").generate(); }
        catch (JOSEException exception) { throw new IllegalStateException(exception); }
    }

    private static String user(String username, String subject) throws JOSEException {
        return "Bearer " + token(username, subject, "blinddating-frontend", "openid profile", ISSUER,
                "profile-service", Instant.now().plusSeconds(300), SIGNING_KEY);
    }

    private static String token(String username, String subject, String client, String scope,
                                String issuer, String audience, Instant expires, RSAKey key) throws JOSEException {
        var jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).keyID("test-key").build(),
                new JWTClaimsSet.Builder().issuer(issuer).audience(audience).subject(subject)
                        .expirationTime(Date.from(expires)).claim("preferred_username", username)
                        .claim("azp", client).claim("scope", scope).build());
        jwt.sign(new RSASSASigner(key));
        return jwt.serialize();
    }
}
