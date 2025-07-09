package com.projects.currency_converter.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
                .title("Currency Converter API")
                .version("1.0.0")
                .description("Rest API for currency conversion with real-time exchange rates")
                .contact(new Contact()
                        .name("Tejasvi R")
                        .email("rozukurthi.tejasvi@gmail.com"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT")));
    }
}
