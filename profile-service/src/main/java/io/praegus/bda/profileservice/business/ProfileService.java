package io.praegus.bda.profileservice.business;

import io.praegus.bda.profileservice.Conflict409Exception;
import io.praegus.bda.profileservice.NotFoundException;
import io.praegus.bda.profileservice.adapter.data.*;
import org.openapitools.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public void createProfile(Profile profileModel) {
        for (ProfileEntity entity : profileRepository.findAll()) {
            if (entity.getUsername().equalsIgnoreCase(profileModel.getUsername())) {
                throw new Conflict409Exception(entity.getUsername(), "User");
            }
        }

        ProfileEntity newProfile = ProfileEntity.builder()
                .username(profileModel.getUsername())
                .firstname(profileModel.getFirstname())
                .lastname(profileModel.getLastname())
                .additionalInfo(profileModel.getAdditionalInfo())
                .personalInformation(PersonalInformationEntity.builder().build())
                .preferences(PreferencesEntity.builder().build())
                .dislikes(DislikesEntity.builder().build())
                .build();
        profileRepository.save(newProfile);
    }

    public List<Profile> getAllProfiles() {

        List<ProfileEntity> profileDatabaseList = profileRepository.findAll();
        List<Profile> resultProfileModels = new ArrayList<>();

        for (ProfileEntity profileEntity : profileDatabaseList) {
            resultProfileModels.add(mapEntityToProfile(profileEntity));
        }
        return resultProfileModels;

    }

    public void deleteProfile(String username) {
        profileRepository.deleteById(username);
    }

    public Profile getProfile(String username) {

        Optional<ProfileEntity> profileEntity = profileRepository.findById(username);

        if(profileEntity.isEmpty()){
            throw new NotFoundException(username, "User");
        }

        return mapEntityToProfile(profileEntity.get());
    }

    public void updateProfile(String username, Profile profileModel) {

        Optional<ProfileEntity> profileEntity = profileRepository.findById(username);

        //profileEntity.get().setUsername(profileModel.getUsername()); #TODO mooie error van maken want usec mag niet worden geupdate
        ProfileEntity updatedProfileEntity = profileEntity.map((entity) -> {
            entity.setFirstname(profileModel.getFirstname());
            entity.setLastname(profileModel.getLastname());
            entity.setAdditionalInfo(profileModel.getAdditionalInfo());

            entity.getPersonalInformation().setPets(profileModel.getPersonalInformation().getPets());
            entity.getPersonalInformation().setGender(profileModel.getPersonalInformation().getGender().getValue());
            entity.getPersonalInformation().setSports(profileModel.getPersonalInformation().getSports());
            entity.getPersonalInformation().setTattoos(profileModel.getPersonalInformation().getTattoos());
            entity.getPersonalInformation().setFavoriteColor(profileModel.getPersonalInformation().getFavoriteColor());
            entity.getPersonalInformation().setHairColor(profileModel.getPersonalInformation().getHairColor().getValue());
            entity.getPersonalInformation().setMusicGenres(profileModel.getPersonalInformation().getMusicGenres().stream().map(String::valueOf).collect(Collectors.joining(",")));

            entity.getPreferences().setPets(profileModel.getPreferences().getPets());
            entity.getPreferences().setGender(profileModel.getPreferences().getGender().getValue());
            entity.getPreferences().setSports(profileModel.getPreferences().getSports());
            entity.getPreferences().setTattoos(profileModel.getPreferences().getTattoos());
            entity.getPreferences().setFavoriteColor(profileModel.getPreferences().getFavoriteColor());
            entity.getPreferences().setHairColor(profileModel.getPreferences().getHairColor().getValue());
            entity.getPreferences().setMusicGenres(profileModel.getPreferences().getMusicGenres().stream().map(String::valueOf).collect(Collectors.joining(",")));

            entity.getDislikes().setPets(profileModel.getDislikes().getPets());
            entity.getDislikes().setGender(profileModel.getDislikes().getGender().getValue());
            entity.getDislikes().setSports(profileModel.getDislikes().getSports());
            entity.getDislikes().setTattoos(profileModel.getDislikes().getTattoos());
            entity.getDislikes().setFavoriteColor(profileModel.getDislikes().getFavoriteColor());
            entity.getDislikes().setHairColor(profileModel.getDislikes().getHairColor().getValue());
            entity.getDislikes().setMusicGenres(profileModel.getDislikes().getMusicGenres().stream().map(String::valueOf).collect(Collectors.joining(",")));

            return entity;
        }).orElseThrow(() -> new NotFoundException(username, "User"));

        profileRepository.save(updatedProfileEntity);

    }

    private Profile mapEntityToProfile(ProfileEntity profileEntity) {
        var builder =  Profile.builder()
                .username(profileEntity.getUsername())
                .firstname(profileEntity.getFirstname())
                .lastname(profileEntity.getLastname())
                .additionalInfo(profileEntity.getAdditionalInfo());

        builder.personalInformation(Characteristics.builder()
                .gender(profileEntity.getPersonalInformation().getGender() != null ? Gender.fromValue(profileEntity.getPersonalInformation().getGender()) : null)
                .favoriteColor(profileEntity.getPersonalInformation().getFavoriteColor())
                .hairColor(profileEntity.getPersonalInformation().getHairColor() != null ? HairColor.fromValue(profileEntity.getPersonalInformation().getHairColor()) : null)
                .pets(profileEntity.getPersonalInformation().isPets())
                .musicGenres(profileEntity.getPersonalInformation().getMusicGenres() != null ? Arrays.stream(profileEntity.getPersonalInformation().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getPersonalInformation().isTattoos())
                .sports(profileEntity.getPersonalInformation().getSports())
                .build());

        builder.preferences(Characteristics.builder()
                .gender(profileEntity.getPreferences().getGender() != null ? Gender.fromValue(profileEntity.getPreferences().getGender()) : null)
                .favoriteColor(profileEntity.getPreferences().getFavoriteColor())
                .hairColor(profileEntity.getPreferences().getHairColor() != null ? HairColor.fromValue(profileEntity.getPreferences().getHairColor()) : null)
                .pets(profileEntity.getPreferences().isPets())
                .musicGenres(profileEntity.getPreferences().getMusicGenres() != null ? Arrays.stream(profileEntity.getPreferences().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getPreferences().isTattoos())
                .sports(profileEntity.getPreferences().getSports())
                .build());

        builder.dislikes(Characteristics.builder()
                .gender(profileEntity.getDislikes().getGender() != null ? Gender.fromValue(profileEntity.getDislikes().getGender()) : null)
                .favoriteColor(profileEntity.getDislikes().getFavoriteColor())
                .hairColor(profileEntity.getDislikes().getHairColor() != null ? HairColor.fromValue(profileEntity.getDislikes().getHairColor()) : null)
                .pets(profileEntity.getDislikes().isPets())
                .musicGenres(profileEntity.getDislikes().getMusicGenres() != null ? Arrays.stream(profileEntity.getDislikes().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getDislikes().isTattoos())
                .sports(profileEntity.getDislikes().getSports())
                .build());

        return builder.build();
    }
}
