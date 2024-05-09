package com.example.demo.connector.model;

import com.example.demo.config.LocalDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class Time {

  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonProperty("updatedISO")
  private LocalDateTime updatedISO;
}
