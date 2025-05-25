package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.model.Gender;
import org.openapitools.model.HairColor;
import org.openapitools.model.MusicGenre;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Characteristics
 */
@lombok.Builder @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-25T09:09:14.850999+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public class Characteristics {

  private Gender gender;

  private String favoriteColor;

  private Boolean pets;

  private HairColor hairColor;

  private Boolean tattoos;

  private String sports;

  @Valid
  private List<MusicGenre> musicGenres = new ArrayList<>();

  public Characteristics gender(Gender gender) {
    this.gender = gender;
    return this;
  }

  /**
   * Get gender
   * @return gender
   */
  @Valid 
  @Schema(name = "gender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gender")
  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Characteristics favoriteColor(String favoriteColor) {
    this.favoriteColor = favoriteColor;
    return this;
  }

  /**
   * Get favoriteColor
   * @return favoriteColor
   */
  
  @Schema(name = "favoriteColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("favoriteColor")
  public String getFavoriteColor() {
    return favoriteColor;
  }

  public void setFavoriteColor(String favoriteColor) {
    this.favoriteColor = favoriteColor;
  }

  public Characteristics pets(Boolean pets) {
    this.pets = pets;
    return this;
  }

  /**
   * Get pets
   * @return pets
   */
  
  @Schema(name = "pets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pets")
  public Boolean getPets() {
    return pets;
  }

  public void setPets(Boolean pets) {
    this.pets = pets;
  }

  public Characteristics hairColor(HairColor hairColor) {
    this.hairColor = hairColor;
    return this;
  }

  /**
   * Get hairColor
   * @return hairColor
   */
  @Valid 
  @Schema(name = "hairColor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hairColor")
  public HairColor getHairColor() {
    return hairColor;
  }

  public void setHairColor(HairColor hairColor) {
    this.hairColor = hairColor;
  }

  public Characteristics tattoos(Boolean tattoos) {
    this.tattoos = tattoos;
    return this;
  }

  /**
   * Get tattoos
   * @return tattoos
   */
  
  @Schema(name = "tattoos", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tattoos")
  public Boolean getTattoos() {
    return tattoos;
  }

  public void setTattoos(Boolean tattoos) {
    this.tattoos = tattoos;
  }

  public Characteristics sports(String sports) {
    this.sports = sports;
    return this;
  }

  /**
   * Get sports
   * @return sports
   */
  
  @Schema(name = "sports", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sports")
  public String getSports() {
    return sports;
  }

  public void setSports(String sports) {
    this.sports = sports;
  }

  public Characteristics musicGenres(List<MusicGenre> musicGenres) {
    this.musicGenres = musicGenres;
    return this;
  }

  public Characteristics addMusicGenresItem(MusicGenre musicGenresItem) {
    if (this.musicGenres == null) {
      this.musicGenres = new ArrayList<>();
    }
    this.musicGenres.add(musicGenresItem);
    return this;
  }

  /**
   * Get musicGenres
   * @return musicGenres
   */
  @Valid 
  @Schema(name = "musicGenres", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("musicGenres")
  public List<MusicGenre> getMusicGenres() {
    return musicGenres;
  }

  public void setMusicGenres(List<MusicGenre> musicGenres) {
    this.musicGenres = musicGenres;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Characteristics characteristics = (Characteristics) o;
    return Objects.equals(this.gender, characteristics.gender) &&
        Objects.equals(this.favoriteColor, characteristics.favoriteColor) &&
        Objects.equals(this.pets, characteristics.pets) &&
        Objects.equals(this.hairColor, characteristics.hairColor) &&
        Objects.equals(this.tattoos, characteristics.tattoos) &&
        Objects.equals(this.sports, characteristics.sports) &&
        Objects.equals(this.musicGenres, characteristics.musicGenres);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gender, favoriteColor, pets, hairColor, tattoos, sports, musicGenres);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Characteristics {\n");
    sb.append("    gender: ").append(toIndentedString(gender)).append("\n");
    sb.append("    favoriteColor: ").append(toIndentedString(favoriteColor)).append("\n");
    sb.append("    pets: ").append(toIndentedString(pets)).append("\n");
    sb.append("    hairColor: ").append(toIndentedString(hairColor)).append("\n");
    sb.append("    tattoos: ").append(toIndentedString(tattoos)).append("\n");
    sb.append("    sports: ").append(toIndentedString(sports)).append("\n");
    sb.append("    musicGenres: ").append(toIndentedString(musicGenres)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

