package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.model.Characteristics;
import org.openapitools.model.RomanticDate;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Profile
 */
@lombok.Builder @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-26T19:57:03.642532200+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public class Profile {

  private String username;

  private String firstname;

  private String lastname;

  private String additionalInfo;

  private Characteristics personalInformation;

  private Characteristics dislikes;

  private Characteristics preferences;

  @Valid
  private List<@Valid RomanticDate> dates = new ArrayList<>();

  public Profile() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Profile(String username) {
    this.username = username;
  }

  public Profile username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  @NotNull @Pattern(regexp = "^[a-zA-Z0-9]*$") @Size(min = 1, max = 20) 
  @Schema(name = "username", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Profile firstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  /**
   * Get firstname
   * @return firstname
   */
  @Size(max = 20) 
  @Schema(name = "firstname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstname")
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public Profile lastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  /**
   * Get lastname
   * @return lastname
   */
  @Size(max = 20) 
  @Schema(name = "lastname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastname")
  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Profile additionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
    return this;
  }

  /**
   * Get additionalInfo
   * @return additionalInfo
   */
  @Size(max = 20) 
  @Schema(name = "additionalInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("additionalInfo")
  public String getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(String additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  public Profile personalInformation(Characteristics personalInformation) {
    this.personalInformation = personalInformation;
    return this;
  }

  /**
   * Get personalInformation
   * @return personalInformation
   */
  @Valid 
  @Schema(name = "personalInformation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("personalInformation")
  public Characteristics getPersonalInformation() {
    return personalInformation;
  }

  public void setPersonalInformation(Characteristics personalInformation) {
    this.personalInformation = personalInformation;
  }

  public Profile dislikes(Characteristics dislikes) {
    this.dislikes = dislikes;
    return this;
  }

  /**
   * Get dislikes
   * @return dislikes
   */
  @Valid 
  @Schema(name = "dislikes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dislikes")
  public Characteristics getDislikes() {
    return dislikes;
  }

  public void setDislikes(Characteristics dislikes) {
    this.dislikes = dislikes;
  }

  public Profile preferences(Characteristics preferences) {
    this.preferences = preferences;
    return this;
  }

  /**
   * Get preferences
   * @return preferences
   */
  @Valid 
  @Schema(name = "preferences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferences")
  public Characteristics getPreferences() {
    return preferences;
  }

  public void setPreferences(Characteristics preferences) {
    this.preferences = preferences;
  }

  public Profile dates(List<@Valid RomanticDate> dates) {
    this.dates = dates;
    return this;
  }

  public Profile addDatesItem(RomanticDate datesItem) {
    if (this.dates == null) {
      this.dates = new ArrayList<>();
    }
    this.dates.add(datesItem);
    return this;
  }

  /**
   * Get dates
   * @return dates
   */
  @Valid 
  @Schema(name = "dates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dates")
  public List<@Valid RomanticDate> getDates() {
    return dates;
  }

  public void setDates(List<@Valid RomanticDate> dates) {
    this.dates = dates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Profile profile = (Profile) o;
    return Objects.equals(this.username, profile.username) &&
        Objects.equals(this.firstname, profile.firstname) &&
        Objects.equals(this.lastname, profile.lastname) &&
        Objects.equals(this.additionalInfo, profile.additionalInfo) &&
        Objects.equals(this.personalInformation, profile.personalInformation) &&
        Objects.equals(this.dislikes, profile.dislikes) &&
        Objects.equals(this.preferences, profile.preferences) &&
        Objects.equals(this.dates, profile.dates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, firstname, lastname, additionalInfo, personalInformation, dislikes, preferences, dates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Profile {\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
    sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
    sb.append("    additionalInfo: ").append(toIndentedString(additionalInfo)).append("\n");
    sb.append("    personalInformation: ").append(toIndentedString(personalInformation)).append("\n");
    sb.append("    dislikes: ").append(toIndentedString(dislikes)).append("\n");
    sb.append("    preferences: ").append(toIndentedString(preferences)).append("\n");
    sb.append("    dates: ").append(toIndentedString(dates)).append("\n");
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

