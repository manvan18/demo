package com.example.demo.connector;

import com.example.demo.connector.model.CoinDeskResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(
    url = "https://api.coindesk.com",
    value = "coin-desk-connector",
    path = "v1")
public interface CoinDeskConnector {

  @GetMapping("/bpi/currentprice.json")
  CoinDeskResponse GetCurrentPrice();

}
