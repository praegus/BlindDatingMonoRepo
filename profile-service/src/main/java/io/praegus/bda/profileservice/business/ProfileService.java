package io.praegus.bda.profileservice.business;

import io.micrometer.common.util.StringUtils;
import io.praegus.bda.profileservice.Conflict409Exception;
import io.praegus.bda.profileservice.NotFoundException;
import io.praegus.bda.profileservice.adapter.data.*;
import io.praegus.bda.profileservice.adapter.location.LocationServiceAdapter;
import org.openapitools.model.*;
import org.openapitools.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private LocationServiceAdapter locationServiceAdapter;

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
                .address(AddressEntity.builder().build())
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

            if (hasAddressChanged(entity.getAddress(), profileModel.getAddress())) {
                var locationAddress = new org.openapitools.client.model.Address();
                locationAddress.setCity(profileModel.getAddress().getCity());
                locationAddress.setStreet(profileModel.getAddress().getStreet());
                locationAddress.setStreetNumber(profileModel.getAddress().getStreetNumber());
                locationAddress.setPostalCode(profileModel.getAddress().getPostalCode());

                var retrievedAddress = locationServiceAdapter.retrieveLocation(locationAddress);
                entity.getAddress().setValid(retrievedAddress.getValid());
                entity.getAddress().setLongitude(retrievedAddress.getLongitude());
                entity.getAddress().setLatitude(retrievedAddress.getLatitude());
            }

            entity.getAddress().setPostalCode(profileModel.getAddress().getPostalCode());
            entity.getAddress().setStreet(profileModel.getAddress().getStreet());
            entity.getAddress().setStreetNumber(profileModel.getAddress().getStreetNumber());
            entity.getAddress().setCity(profileModel.getAddress().getCity());

            entity.getPersonalInformation().setPets(profileModel.getPersonalInformation().getPets());
            entity.getPersonalInformation().setGender(Optional.ofNullable(profileModel.getPersonalInformation().getGender()).map(Gender::getValue).orElse(""));
            entity.getPersonalInformation().setSports(profileModel.getPersonalInformation().getSports());
            entity.getPersonalInformation().setTattoos(profileModel.getPersonalInformation().getTattoos());
            entity.getPersonalInformation().setFavoriteColor(profileModel.getPersonalInformation().getFavoriteColor());
            entity.getPersonalInformation().setHairColor(Optional.ofNullable(profileModel.getPersonalInformation().getHairColor()).map(HairColor::getValue).orElse(""));
            entity.getPersonalInformation().setMusicGenres(Optional.ofNullable(profileModel.getPersonalInformation().getMusicGenres()).map(g -> g.stream().map(String::valueOf).collect(Collectors.joining(","))).orElse(""));

            entity.getPreferences().setPets(profileModel.getPreferences().getPets());
            entity.getPreferences().setGender(Optional.ofNullable(profileModel.getPreferences().getGender()).map(Gender::getValue).orElse(""));
            entity.getPreferences().setSports(profileModel.getPreferences().getSports());
            entity.getPreferences().setTattoos(profileModel.getPreferences().getTattoos());
            entity.getPreferences().setFavoriteColor(profileModel.getPreferences().getFavoriteColor());
            entity.getPreferences().setHairColor(Optional.ofNullable(profileModel.getPreferences().getHairColor()).map(HairColor::getValue).orElse(""));
            entity.getPreferences().setMusicGenres(Optional.ofNullable(profileModel.getPreferences().getMusicGenres()).map(g -> g.stream().map(String::valueOf).collect(Collectors.joining(","))).orElse(""));

            entity.getDislikes().setPets(profileModel.getDislikes().getPets());
            entity.getDislikes().setGender(Optional.ofNullable(profileModel.getDislikes().getGender()).map(Gender::getValue).orElse(""));
            entity.getDislikes().setSports(profileModel.getDislikes().getSports());
            entity.getDislikes().setTattoos(profileModel.getDislikes().getTattoos());
            entity.getDislikes().setFavoriteColor(profileModel.getDislikes().getFavoriteColor());
            entity.getDislikes().setHairColor(Optional.ofNullable(profileModel.getDislikes().getHairColor()).map(HairColor::getValue).orElse(""));
            entity.getDislikes().setMusicGenres(Optional.ofNullable(profileModel.getDislikes().getMusicGenres()).map(g -> g.stream().map(String::valueOf).collect(Collectors.joining(","))).orElse(""));

            return entity;
        }).orElseThrow(() -> new NotFoundException(username, "User"));

        profileRepository.save(updatedProfileEntity);

    }

    private boolean hasAddressChanged(AddressEntity oldAddress, Address newAddress) {
        if (oldAddress == null && newAddress != null) {
            return true;
        }
        return !(Objects.equals(oldAddress.getStreet(), newAddress.getStreet())
                && Objects.equals(oldAddress.getStreetNumber(), newAddress.getStreetNumber())
                && Objects.equals(oldAddress.getCity(), newAddress.getCity())
                && Objects.equals(oldAddress.getPostalCode(), newAddress.getPostalCode())
        );
    }

    private Profile mapEntityToProfile(ProfileEntity profileEntity) {
        var builder =  Profile.builder()
                .username(profileEntity.getUsername())
                .firstname(profileEntity.getFirstname())
                .lastname(profileEntity.getLastname())
                .additionalInfo(profileEntity.getAdditionalInfo());

        builder.address(Address.builder()
                .city(profileEntity.getAddress().getCity())
                .street(profileEntity.getAddress().getStreet())
                .streetNumber(profileEntity.getAddress().getStreetNumber())
                .postalCode(profileEntity.getAddress().getPostalCode())
                .valid(profileEntity.getAddress().isValid())
                .latitude(profileEntity.getAddress().getLatitude())
                .longitude(profileEntity.getAddress().getLongitude())
                .build());

        builder.personalInformation(Characteristics.builder()
                .gender(StringUtils.isNotEmpty(profileEntity.getPersonalInformation().getGender()) ? Gender.fromValue(profileEntity.getPersonalInformation().getGender()) : null)
                .favoriteColor(profileEntity.getPersonalInformation().getFavoriteColor())
                .hairColor(StringUtils.isNotEmpty(profileEntity.getPersonalInformation().getHairColor()) ? HairColor.fromValue(profileEntity.getPersonalInformation().getHairColor()) : null)
                .pets(profileEntity.getPersonalInformation().isPets())
                .musicGenres(StringUtils.isNotEmpty(profileEntity.getPersonalInformation().getMusicGenres()) ? Arrays.stream(profileEntity.getPersonalInformation().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getPersonalInformation().isTattoos())
                .sports(profileEntity.getPersonalInformation().getSports())
                .build());

        builder.preferences(Characteristics.builder()
                .gender(StringUtils.isNotEmpty(profileEntity.getPreferences().getGender()) ? Gender.fromValue(profileEntity.getPreferences().getGender()) : null)
                .favoriteColor(profileEntity.getPreferences().getFavoriteColor())
                .hairColor(StringUtils.isNotEmpty(profileEntity.getPreferences().getHairColor()) ? HairColor.fromValue(profileEntity.getPreferences().getHairColor()) : null)
                .pets(profileEntity.getPreferences().isPets())
                .musicGenres(StringUtils.isNotEmpty(profileEntity.getPreferences().getMusicGenres()) ? Arrays.stream(profileEntity.getPreferences().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getPreferences().isTattoos())
                .sports(profileEntity.getPreferences().getSports())
                .build());

        builder.dislikes(Characteristics.builder()
                .gender(StringUtils.isNotEmpty(profileEntity.getDislikes().getGender()) ? Gender.fromValue(profileEntity.getDislikes().getGender()) : null)
                .favoriteColor(profileEntity.getDislikes().getFavoriteColor())
                .hairColor(StringUtils.isNotEmpty(profileEntity.getDislikes().getHairColor()) ? HairColor.fromValue(profileEntity.getDislikes().getHairColor()) : null)
                .pets(profileEntity.getDislikes().isPets())
                .musicGenres(StringUtils.isNotEmpty(profileEntity.getDislikes().getMusicGenres()) ? Arrays.stream(profileEntity.getDislikes().getMusicGenres().split(",")).map(MusicGenre::fromValue).toList() : null)
                .tattoos(profileEntity.getDislikes().isTattoos())
                .sports(profileEntity.getDislikes().getSports())
                .build());

        builder.dates(profileEntity.getDates().stream().map(d -> {
            var address = new Address(d.getAddressStreet(), d.getAddressStreetNumber(), d.getAddressPostalCode(), d.getAddressCity(), null, null, null);
            return new RomanticDate(address, d.getDateTime().toOffsetDateTime(), d.getItemToBring());
        }).toList());

        return builder.build();
    }

    public void addDateToProfile(String username, Date date) {
        var profile = profileRepository.findById(username).orElseThrow();
        var newDateEntity = DateEntity.builder()
                .addressPostalCode(date.location().getPostalCode())
                .addressStreet(date.location().getStreet())
                .addressStreetNumber(date.location().getStreetNumber())
                .addressCity(date.location().getCity())
                .dateTime(date.time())
                .itemToBring(date.objectToBring())
                .build();
        profile.getDates().add(newDateEntity);

        profileRepository.save(profile);
    }
}
