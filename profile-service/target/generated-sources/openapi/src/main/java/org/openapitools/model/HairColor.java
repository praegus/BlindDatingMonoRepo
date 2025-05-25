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
 * Gets or Sets HairColor
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-25T09:09:14.850999+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public enum HairColor {
  
  BLOND("BLOND"),
  
  BROWN("BROWN"),
  
  BLACK("BLACK"),
  
  RED("RED"),
  
  FALSE("false"),
  
  GRAY("GRAY"),
  
  PINK("PINK");

  private String value;

  HairColor(String value) {
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
  public static HairColor fromValue(String value) {
    for (HairColor b : HairColor.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

