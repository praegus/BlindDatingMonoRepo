package org.openapitools.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.model.FieldError;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ErrorResponse
 */
@lombok.Builder @lombok.AllArgsConstructor

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-25T09:09:14.850999+02:00[Europe/Budapest]", comments = "Generator version: 7.8.0")
public class ErrorResponse {

  private String type;

  private String title;

  private Integer status;

  private String detail;

  private String instance;

  @Valid
  private List<@Valid FieldError> errors = new ArrayList<>();

  public ErrorResponse type(String type) {
    this.type = type;
    return this;
  }

  /**
   * a URI identifier that categorizes the error
   * @return type
   */
  
  @Schema(name = "type", description = "a URI identifier that categorizes the error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ErrorResponse title(String title) {
    this.title = title;
    return this;
  }

  /**
   * a brief, human-readable message about the error
   * @return title
   */
  
  @Schema(name = "title", description = "a brief, human-readable message about the error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ErrorResponse status(Integer status) {
    this.status = status;
    return this;
  }

  /**
   * the HTTP response code (optional)
   * @return status
   */
  
  @Schema(name = "status", description = "the HTTP response code (optional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public ErrorResponse detail(String detail) {
    this.detail = detail;
    return this;
  }

  /**
   * a human-readable explanation of the error
   * @return detail
   */
  
  @Schema(name = "detail", description = "a human-readable explanation of the error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detail")
  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }

  public ErrorResponse instance(String instance) {
    this.instance = instance;
    return this;
  }

  /**
   * a URI that identifies the specific occurrence of the error
   * @return instance
   */
  
  @Schema(name = "instance", description = "a URI that identifies the specific occurrence of the error", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instance")
  public String getInstance() {
    return instance;
  }

  public void setInstance(String instance) {
    this.instance = instance;
  }

  public ErrorResponse errors(List<@Valid FieldError> errors) {
    this.errors = errors;
    return this;
  }

  public ErrorResponse addErrorsItem(FieldError errorsItem) {
    if (this.errors == null) {
      this.errors = new ArrayList<>();
    }
    this.errors.add(errorsItem);
    return this;
  }

  /**
   * Get errors
   * @return errors
   */
  @Valid 
  @Schema(name = "errors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("errors")
  public List<@Valid FieldError> getErrors() {
    return errors;
  }

  public void setErrors(List<@Valid FieldError> errors) {
    this.errors = errors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ErrorResponse errorResponse = (ErrorResponse) o;
    return Objects.equals(this.type, errorResponse.type) &&
        Objects.equals(this.title, errorResponse.title) &&
        Objects.equals(this.status, errorResponse.status) &&
        Objects.equals(this.detail, errorResponse.detail) &&
        Objects.equals(this.instance, errorResponse.instance) &&
        Objects.equals(this.errors, errorResponse.errors);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, title, status, detail, instance, errors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ErrorResponse {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    detail: ").append(toIndentedString(detail)).append("\n");
    sb.append("    instance: ").append(toIndentedString(instance)).append("\n");
    sb.append("    errors: ").append(toIndentedString(errors)).append("\n");
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

