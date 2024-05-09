package com.example.demo.exception.model;

import java.util.Arrays;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class ApiError {

  private final List<ApiErrorEntry> errors;

  public ApiError(ApiErrorEntry... errorEntries) {
    errors = Arrays.asList(errorEntries);
  }

  public ApiError(List<ApiErrorEntry> apiErrorEntries) {
    errors = apiErrorEntries;
  }

}

