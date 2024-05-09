package com.example.demo.service;

import com.example.demo.service.model.Currency;
import java.text.ParseException;
import java.util.List;

public interface CoinDeskService {

  List<Currency> getCurrencies();
  Currency createCurrency(Currency currency) throws ParseException;

  void deleteCurrency(String code);

  Currency updateCurrency(Currency currency) throws ParseException;



}
