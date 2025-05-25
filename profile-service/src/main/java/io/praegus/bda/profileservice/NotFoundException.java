package io.praegus.bda.profileservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, String entityType) {
        super(entityType + " '" + entity + "' does not exist.");
    }
}
