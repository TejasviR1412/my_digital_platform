package com.projects.currency_converter.controller;

import com.projects.currency_converter.service.CurrencyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyApiContoller.class)
class CurrencyApiContollerTest {

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
    @DisplayName("POST /api/currency/convert should return converted amount")
    void testConvert() throws Exception {
        Mockito.when(currencyService.convert("USD", "EUR", 100.0)).thenReturn(85.5);

        mockMvc.perform(post("/api/currency/convert")
                        .param("from", "USD")
                        .param("to", "EUR")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(content().string("85.5"));
    }
}