package io.praegus.bda.matchingservice.business;

import java.util.Map;

public record MatchStatus(Map<String, Boolean> matchAcknowledgement) {
}
