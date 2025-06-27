package io.praegus.bda.matchingservice.business;

import com.example.Match;
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

    @Scheduled(fixedRate = 10, timeUnit = TimeUnit.SECONDS)
    public void attemptMatching() {
        createMatches();
    }

    public void createMatches() {
        var profiles = profileServiceAdapter.findAllProfiles();

        if (profiles.size() < 2) {
            logger.info("Not enough profiles to create matches. Found {} profiles", profiles.size());
            return;
        }

        var matches = findMatches(profiles);

        logger.info("Found {} matches", matches.size());

        matches.forEach(kafkaProducer::sendMessage);
    }

    private List<Match> findMatches(List<Profile> profiles) {
        var profilesToCheckAgainst = new ArrayList<>(profiles);
        var matches = new ArrayList<Match>();
        profiles.forEach(profile -> {
            profilesToCheckAgainst.remove(profile);
            profilesToCheckAgainst.forEach(profileToCheckAgainst -> {
                var matchingScore = matchingScoreService.determineMatchingScore(profile, profileToCheckAgainst);
                logger.info("username {} and username {} matched for {}", profile.getUsername(), profileToCheckAgainst.getUsername(), matchingScore);
                if (matchingScore >= 70) {
                    matches.add(new Match(profile.getUsername(), profileToCheckAgainst.getUsername()));
                }
            });
        });
        return matches;
    }
}
