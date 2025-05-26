package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets MusicGenre
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-26T19:57:03.642532200+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public enum MusicGenre {
  
  ROCK("ROCK"),
  
  METAL("METAL"),
  
  DEATHMETAL("DEATHMETAL"),
  
  TRASHMETAL("TRASHMETAL"),
  
  BALLROOM("BALLROOM"),
  
  CLASSICAL("CLASSICAL"),
  
  COUNTRY("COUNTRY"),
  
  DANCE("DANCE"),
  
  ELECTRONIC("ELECTRONIC"),
  
  FUNK("FUNK"),
  
  HIP_HOP("HIP_HOP"),
  
  JAZZ("JAZZ"),
  
  LATIN("LATIN"),
  
  POP("POP"),
  
  RB("RB"),
  
  REGGAE("REGGAE"),
  
  UNKNOWN("UNKNOWN"),
  
  WORLD("WORLD");

  private String value;

  MusicGenre(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MusicGenre fromValue(String value) {
    for (MusicGenre b : MusicGenre.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

