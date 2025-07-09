package com.projects.currency_converter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix="exchange.api")
public class ExchangeApiProperties {
    private String url;
    private String key;
   // private String basecurrency;
}