package io.praegus.bda.matchingservice.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.praegus.bda.matchingservice.adapter.kafka.KafkaProducer;
import io.praegus.bda.matchingservice.adapter.profile.ProfileServiceAdapter;
import lombok.RequiredArgsConstructor;
import org.openapitools.client.model.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MatchingService {

    Logger logger = LoggerFactory.getLogger(MatchingService.class);

    private final ProfileServiceAdapter profileServiceAdapter;
    private final KafkaProducer kafkaProducer;
    private final MatchingScoreService matchingScoreService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void attemptMatching() {
        createMatches();
    }

    public void createMatches() {
        var profiles = profileServiceAdapter.findAllProfiles();

        if (profiles.size() < 2) {
            logger.info("Not enough profiles to create matches. Found {} profiles", profiles.size());
            kafkaProducer.sendMessage("{\"personA\":\"Fin\",\"personB\":\"Sandra\"}");
            return;
        }

        var matches = findMatches(profiles);

        logger.info("Found {} matches", matches.size());

        matches.forEach(match -> {
            try {
                kafkaProducer.sendMessage(objectMapper.writeValueAsString(match));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<Match> findMatches(List<Profile> profiles) {
        var profilesToCheckAgainst = new ArrayList<>(profiles);
        var matches = new ArrayList<Match>();
        profiles.forEach(profile -> {
            profilesToCheckAgainst.remove(profile);
            profilesToCheckAgainst.forEach(profileToCheckAgainst -> {
                var matchingScore = matchingScoreService.determineMatchingScore(profile, profileToCheckAgainst);
                if (matchingScore >= 70) {
                    matches.add(new Match(profile.getUsername(), profileToCheckAgainst.getUsername()));
                }
            });
        });
        return matches;
    }
}
