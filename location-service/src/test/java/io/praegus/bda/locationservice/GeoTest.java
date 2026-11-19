package io.praegus.bda.locationservice;

import io.praegus.bda.locationservice.adapter.GeoApifyClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GeoTest {

    @Autowired
    GeoApifyClient geoApifyClient;

    @Test
    void myTest() {
        var response = geoApifyClient.getRestaurants(new BigDecimal(5.802245), new BigDecimal(51.835785));
        assertThat(response).isNotEmpty();
    }
}
