package io.praegus.bda.profileservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class Conflict409Exception extends RuntimeException {

    public Conflict409Exception(String entity, String entityType) {
        super(entityType + " '" + entity + "' already exists.");
    }
}
