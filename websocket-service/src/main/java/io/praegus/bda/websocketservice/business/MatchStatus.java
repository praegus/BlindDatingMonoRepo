package io.praegus.bda.websocketservice.business;

import java.util.Map;

public record MatchStatus(Map<String, Boolean> matchAcknowledgement) {
}
