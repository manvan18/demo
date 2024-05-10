package com.example.demo.connector.model;

import com.example.demo.config.BigDecimalDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@ToString
public class CurrencyInfo {
  private String code;
  private String symbol;
  @JsonDeserialize(using = BigDecimalDeserializer.class)
  private BigDecimal rate;
  private String description;
}
