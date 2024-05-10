package com.example.demo;

import java.util.TimeZone;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients("com.example.demo.connector")
@EnableScheduling
public class DemoApplication {

  public static void main(String[] args) {

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    SpringApplication.run(DemoApplication.class, args);
  }

}
