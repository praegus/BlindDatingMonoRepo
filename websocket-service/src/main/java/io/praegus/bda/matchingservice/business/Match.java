package io.praegus.bda.matchingservice.business;

public record Match(String personA, String personB) {
    public String toKey() {
        return this.personA() + ":" + this.personB();
    }
}
