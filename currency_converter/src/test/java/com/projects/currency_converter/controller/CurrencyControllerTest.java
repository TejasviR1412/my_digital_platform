package com.projects.currency_converter.controller;

import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public CurrencyService currencyService() {
            return Mockito.mock(CurrencyService.class);
        }
    }

    @Test
    @DisplayName("GET /currency/converter should return converter form with empty request")
    void testShowConverterForm() throws Exception {
        mockMvc.perform(get("/currency/converter"))
                .andExpect(status().isOk())
                .andExpect(view().name("currency-converter"))
                .andExpect(model().attributeExists("conversionRequest"))
                .andExpect(model().attribute("conversionRequest", hasProperty("fromCurrency", nullValue())))
                .andExpect(model().attribute("conversionRequest", hasProperty("toCurrency", nullValue())))
                .andExpect(model().attribute("conversionRequest", hasProperty("amount", is(0.0))));
    }

    @Test
    @DisplayName("POST /currency/convert should convert and show result on success")
    void testConvertCurrencySuccess() throws Exception {
        Mockito.when(currencyService.convert("USD", "EUR", 100.0)).thenReturn(85.5);

        mockMvc.perform(post("/currency/convert")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("currency-converter"))
                .andExpect(model().attributeExists("conversionRequest"))
                .andExpect(model().attributeExists("conversionSuccess"))
                .andExpect(model().attribute("conversionSuccess", true))
                .andExpect(model().attribute("conversionRequest", hasProperty("convertedAmount", is(85.5))));
    }

    @Test
    @DisplayName("POST /currency/convert should handle exception and not set conversionSuccess")
    void testConvertCurrencyException() throws Exception {
        Mockito.when(currencyService.convert(anyString(), anyString(), anyDouble()))
                .thenThrow(new RuntimeException("Service failure"));

        mockMvc.perform(post("/currency/convert")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(view().name("currency-converter"))
                .andExpect(model().attributeDoesNotExist("conversionSuccess"));
    }

    @Test
    @DisplayName("GET /currency/rates should return all exchange rates")
    void testShowAllRates() throws Exception {
        ExchangeRate rate1 = new ExchangeRate();
        rate1.setCurrencyPair("USD_EUR");
        rate1.setFromCurrency("USD");
        rate1.setToCurrency("EUR");
        rate1.setRate(BigDecimal.valueOf(0.85));
        rate1.setLocalDateTime(LocalDate.now());

        ExchangeRate rate2 = new ExchangeRate();
        rate2.setCurrencyPair("USD_JPY");
        rate2.setFromCurrency("USD");
        rate2.setToCurrency("JPY");
        rate2.setRate(BigDecimal.valueOf(110.0));
        rate2.setLocalDateTime(LocalDate.now());

        List<ExchangeRate> rates = List.of(rate1, rate2);

        Mockito.when(currencyService.getAllRates()).thenReturn(rates);

        mockMvc.perform(get("/currency/rates"))
                .andExpect(status().isOk())
                .andExpect(view().name("exchange-rates"))
                .andExpect(model().attribute("exchangeRates", hasSize(2)))
                .andExpect(model().attribute("exchangeRates", hasItem(
                        allOf(
                                hasProperty("fromCurrency", is("USD")),
                                hasProperty("toCurrency", is("EUR")),
                                hasProperty("rate", is(BigDecimal.valueOf(0.85)))
                        )
                )))
                .andExpect(model().attribute("exchangeRates", hasItem(
                        allOf(
                                hasProperty("fromCurrency", is("USD")),
                                hasProperty("toCurrency", is("JPY")),
                                hasProperty("rate", is(BigDecimal.valueOf(110.0)))
                        )
                )));
    }

    @Test
    @DisplayName("GET /currency/rates/{baseCurrency} should filter rates by base currency")
    void testShowRatesForCurrency() throws Exception {
        ExchangeRate rate1 = new ExchangeRate();
        rate1.setCurrencyPair("USD_EUR");
        rate1.setFromCurrency("USD");
        rate1.setToCurrency("EUR");
        rate1.setRate(BigDecimal.valueOf(0.85));
        rate1.setLocalDateTime(LocalDate.now());

        ExchangeRate rate2 = new ExchangeRate();
        rate2.setCurrencyPair("USD_JPY");
        rate2.setFromCurrency("USD");
        rate2.setToCurrency("JPY");
        rate2.setRate(BigDecimal.valueOf(110.0));
        rate2.setLocalDateTime(LocalDate.now());

        ExchangeRate rate3 = new ExchangeRate();
        rate3.setCurrencyPair("GBP_EUR");
        rate3.setFromCurrency("GBP");
        rate3.setToCurrency("EUR");
        rate3.setRate(BigDecimal.valueOf(1.15));
        rate3.setLocalDateTime(LocalDate.now());

        List<ExchangeRate> rates = List.of(rate1, rate2, rate3);

        Mockito.when(currencyService.getAllRates()).thenReturn(rates);

        mockMvc.perform(get("/currency/rates/USD"))
                .andExpect(status().isOk())
                .andExpect(view().name("exchange-rates"))
                .andExpect(model().attribute("baseCurrency", "USD"))
                .andExpect(model().attribute("exchangeRates", hasSize(2)))
                .andExpect(model().attribute("exchangeRates", everyItem(
                        hasProperty("fromCurrency", is("USD"))
                )));
    }
}