package com.example.demo.utils;

import com.example.demo.exception.InvalidFormatCurrency;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CommonUtils {

  public static BigDecimal parseString(String rate) {
    String stringValue =rate.replaceAll("[^0-9.]", "");
    // Parse the cleaned string to BigDecimal
    NumberFormat format = NumberFormat.getInstance(Locale.US);
    Number number = null;
    try {
      number = format.parse(stringValue);
    } catch (ParseException e) {
      throw new InvalidFormatCurrency(e.getMessage());
    }
    return BigDecimal.valueOf(number.doubleValue());
  }

}
