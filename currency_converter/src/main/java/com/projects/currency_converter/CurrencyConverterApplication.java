package com.projects.currency_converter;

import com.projects.currency_converter.configuration.ExchangeApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(ExchangeApiProperties.class)
@SpringBootApplication
public class CurrencyConverterApplication {
	public static void main(String[] args) {
		SpringApplication.run(CurrencyConverterApplication.class, args);
	}
}