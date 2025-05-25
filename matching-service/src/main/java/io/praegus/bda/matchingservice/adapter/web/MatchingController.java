package io.praegus.bda.matchingservice.adapter.web;

import io.praegus.bda.matchingservice.business.MatchingService;
import org.openapitools.api.MatchingApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MatchingController implements MatchingApi {

    @Autowired
    private MatchingService matchingService;

    @Override
    public ResponseEntity<String> createMatches() {
        matchingService.createMatches();
        return null;
    }
}
