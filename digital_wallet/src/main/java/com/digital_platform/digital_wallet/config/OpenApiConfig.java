package com.digital_platform.digital_wallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
 @Bean
 public OpenAPI digitalWalletOpenApiConfig(){
  return new OpenAPI()
          .info(new Info()
                  .title("Digital Wallet API")
                  .description("Simple wallet with deposit, withdraw, mini statement and currency conversion support")
                  .version("v1.0.0")
                  .contact(new Contact()
                          .name("Tejasvi R")
                          .url("https://github.com/TejasviR1412")
                          .email("your@email.com"))
          );
 }
}