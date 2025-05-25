package io.praegus.bda.matchingservice.business;

import org.openapitools.client.model.Profile;
import org.springframework.stereotype.Service;

@Service
public class MatchingScoreService {

    public int determineMatchingScore(Profile personA, Profile personB) {
        var score = 0;
        if (personA.getPersonalInformation().getGender() == null
                || personA.getPreferences().getGender() == null
                || personB.getPersonalInformation().getGender() == null
                || personB.getPreferences().getGender() == null
        ) {
            return score;
        }

        if (personA.getPersonalInformation().getGender().equals(personB.getPreferences().getGender())) {
            score += 50;
        }

        if (personB.getPersonalInformation().getGender().equals(personA.getPreferences().getGender())) {
            score += 50;
        }

        return score;
    }
}
