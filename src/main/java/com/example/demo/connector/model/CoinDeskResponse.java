package com.example.demo.connector.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@ToString
public class CoinDeskResponse {

  private Time time;
  private String disclaimer;
  private String chartName;
  private Map<String, CurrencyInfo> bpi;

}


