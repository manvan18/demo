package com.example.demo.connector.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class CoinDeskResponse {

  private Time time;
  private String disclaimer;
  private String chartName;
  private Map<String, CurrencyInfo> bpi;

}


