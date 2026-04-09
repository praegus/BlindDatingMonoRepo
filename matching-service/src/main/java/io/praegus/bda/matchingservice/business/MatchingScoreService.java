package io.praegus.bda.matchingservice.business;

import org.openapitools.client.model.Characteristics;
import org.openapitools.client.model.MusicGenre;
import org.openapitools.client.model.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MatchingScoreService {

    public int determineMatchingScore(Profile a, Profile b) {
        return (determineMatchingScoreFromFirst(a, b) + determineMatchingScoreFromFirst(b, a)) / 2;
    }

    private int determineMatchingScoreFromFirst(Profile a, Profile b) {
        int score = 0;
        int maxScore = 100;

        if (a.getPreferences() == null
                || a.getDislikes() == null
                || b.getPersonalInformation() == null
        ) {
            return score;
        }

        Characteristics prefsA = a.getPreferences();
        Characteristics dislikesA = a.getDislikes();
        Characteristics infoB = b.getPersonalInformation();

        // Gender match
        if (prefsA.getGender() != null && prefsA.getGender().equals(infoB.getGender())) score += 50;
        if (dislikesA.getGender() != null && dislikesA.getGender().equals(infoB.getGender())) score -= 50;

        // Hair color
        if (prefsA.getHairColor() != null && prefsA.getHairColor().equals(infoB.getHairColor())) score += 10;
        if (dislikesA.getHairColor() != null && dislikesA.getHairColor().equals(infoB.getHairColor())) score -= 10;

        // Pets
        if (prefsA.getPets() != null && prefsA.getPets().equals(infoB.getPets())) score += 10;
        if (dislikesA.getPets() != null && dislikesA.getPets().equals(infoB.getPets())) score -= 10;

        // Tattoos
        if (prefsA.getTattoos() != null && prefsA.getTattoos().equals(infoB.getTattoos())) score += 10;
        if (dislikesA.getTattoos() != null && dislikesA.getTattoos().equals(infoB.getTattoos())) score -= 10;

        // Favorite color
        if (prefsA.getFavoriteColor() != null && prefsA.getFavoriteColor().equalsIgnoreCase(infoB.getFavoriteColor())) score += 10;
        if (dislikesA.getFavoriteColor() != null && dislikesA.getFavoriteColor().equalsIgnoreCase(infoB.getFavoriteColor())) score -= 10;

        // Sports
        if (prefsA.getSports() != null && prefsA.getSports().equalsIgnoreCase(infoB.getSports())) score += 10;
        if (dislikesA.getSports() != null && dislikesA.getSports().equalsIgnoreCase(infoB.getSports())) score -= 10;

        // Music genres
        score += calculateGenreScore(prefsA.getMusicGenres(), infoB.getMusicGenres(), 2);
        score -= calculateGenreScore(dislikesA.getMusicGenres(), infoB.getMusicGenres(), 2);

        // Clamp score between 0 and maxScore
        return Math.max(0, Math.min(score, maxScore));
    }

    private static int calculateGenreScore(List<MusicGenre> prefs, List<MusicGenre> actual, int weight) {
        if (prefs == null || actual == null) return 0;
        Set<MusicGenre> intersection = new HashSet<>(prefs);
        intersection.retainAll(actual);
        return intersection.size() * weight;
    }
}
