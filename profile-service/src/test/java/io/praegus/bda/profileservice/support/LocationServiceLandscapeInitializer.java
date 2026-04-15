package io.praegus.bda.profileservice.support;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.springframework.test.context.DynamicPropertyRegistry;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public final class LocationServiceLandscapeInitializer {

    private LocationServiceLandscapeInitializer() {
    }

    public static final WireMockExtension LOCATION_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();

    public static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("endpoints.locationservice", LOCATION_SERVICE::baseUrl);
    }

    public static void reset() {
        LOCATION_SERVICE.resetAll();
    }

    public static void stubUnresolvedLocationLookup() {
        LOCATION_SERVICE.stubFor(post(urlEqualTo("/location"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "valid": false
                                }
                                """)));
    }

    public static void verifyLocationLookupCalled() {
        LOCATION_SERVICE.verify(postRequestedFor(urlEqualTo("/location"))
                .withHeader("Content-Type", equalTo("application/json")));
    }
}
