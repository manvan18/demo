package com.example.demo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {

  @Override
  public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    String stringValue = jsonParser.getText();
    try {
      // Remove any non-numeric characters (like commas)
      stringValue = stringValue.replaceAll("[^0-9.]", "");
      // Parse the cleaned string to BigDecimal
      NumberFormat format = NumberFormat.getInstance(Locale.US);
      Number number = format.parse(stringValue);
      return BigDecimal.valueOf(number.doubleValue());
    } catch (ParseException e) {
      throw new IOException("Failed to parse BigDecimal value", e);
    }
  }
}