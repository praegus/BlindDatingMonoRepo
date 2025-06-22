package io.praegus.bda.locationservice.business;

import org.openapitools.client.model.Address;

import java.time.ZonedDateTime;

public record Date(String personA, String personB, Address location, ZonedDateTime time, String objectToBring) {
}
