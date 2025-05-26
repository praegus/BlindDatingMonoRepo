package io.praegus.bda.matchingservice.business;

import java.time.ZonedDateTime;

public record Date(String personA, String personB, Address location, ZonedDateTime time, String objectToBring) {
}
