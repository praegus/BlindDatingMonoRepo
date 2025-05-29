package io.praegus.bda.dateservice.business;

import java.time.ZonedDateTime;

public record Date(String personA, String personB, Address location, ZonedDateTime time, String objectToBring) {
}
