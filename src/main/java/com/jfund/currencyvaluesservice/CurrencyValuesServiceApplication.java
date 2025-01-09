package com.jfund.currencyvaluesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
public class CurrencyValuesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyValuesServiceApplication.class, args);
    }

}
