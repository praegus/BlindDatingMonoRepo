package io.praegus.bda.profileservice.component;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberComponentTestConfiguration {

    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("profile_service_cucumber_test")
            .withUsername("test")
            .withPassword("test");

    static final WireMockServer LOCATION_SERVICE = new WireMockServer(
            WireMockConfiguration.wireMockConfig().dynamicPort());

    static {
        POSTGRES.start();
        LOCATION_SERVICE.start();
    }

    @DynamicPropertySource
    static void registerLandscapeProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("endpoints.locationservice", LOCATION_SERVICE::baseUrl);
    }
}
