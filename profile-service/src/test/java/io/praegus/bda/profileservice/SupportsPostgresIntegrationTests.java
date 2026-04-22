package io.praegus.bda.profileservice;

import io.praegus.bda.profileservice.support.LocationServiceLandscapeInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class SupportsPostgresIntegrationTests {

    @org.junit.jupiter.api.extension.RegisterExtension
    static final com.github.tomakehurst.wiremock.junit5.WireMockExtension LOCATION_SERVICE =
            LocationServiceLandscapeInitializer.LOCATION_SERVICE;

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("profile_service_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerLandscapeProperties(DynamicPropertyRegistry registry) {
        LocationServiceLandscapeInitializer.registerProperties(registry);
    }

    @BeforeEach
    void resetLandscape() {
        LocationServiceLandscapeInitializer.reset();
    }

    protected final void stubUnresolvedLocationLookup() {
        LocationServiceLandscapeInitializer.stubUnresolvedLocationLookup();
    }

    protected final void verifyLocationLookupCalled() {
        LocationServiceLandscapeInitializer.verifyLocationLookupCalled();
    }
}
