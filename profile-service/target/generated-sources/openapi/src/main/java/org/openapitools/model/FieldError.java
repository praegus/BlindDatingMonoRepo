package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * FieldError
 */
@lombok.Builder @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-26T19:57:03.642532200+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public class FieldError {

  private String field;

  private String error;

  public FieldError field(String field) {
    this.field = field;
    return this;
  }

  /**
   * the field that the error is thrown on
   * @return field
   */
  
  @Schema(name = "field", description = "the field that the error is thrown on", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public FieldError error(String error) {
    this.error = error;
    return this;
  }

  /**
   * the error message
   * @return error
   */
  
  @Schema(name = "error", description = "the error message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("error")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FieldError fieldError = (FieldError) o;
    return Objects.equals(this.field, fieldError.field) &&
        Objects.equals(this.error, fieldError.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(field, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FieldError {\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

