package io.praegus.bda.matchingservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;

public class ProfileServiceWiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final WireMockServer profileService =
            new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        profileService.start();
        WireMock.configureFor(profileService.getOptions().bindAddress(), profileService.port());

        TestPropertyValues.of(
                "endpoints.profileservice=" + profileService.baseUrl()
        ).applyTo(applicationContext);

        stubFor(WireMock.get("/profiles").atPriority(100).willReturn(
                WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")
        ));

        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                profileService.stop();
            }
        });
    }
}
