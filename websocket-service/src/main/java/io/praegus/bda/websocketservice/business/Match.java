package io.praegus.bda.websocketservice.business;

public record Match(String personA, String personB) {
    public String toKey() {
        return this.personA() + ":" + this.personB();
    }
}
