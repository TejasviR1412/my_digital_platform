package com.projects.currency_converter.service;

import com.projects.currency_converter.configuration.ExchangeApiProperties;
import com.projects.currency_converter.repository.ExchangeRateRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@Slf4j
public class ExchangeRateUpdaterServiceTest {
    @Test
    public void test_update_rates_for_base_currency_parses_and_saves_rates(){
        ExchangeRateRepository repository = mock(ExchangeRateRepository.class);

        ExchangeApiProperties properties = new ExchangeApiProperties();
        properties.setKey("test-key");
        properties.setUrl("https://api.example.com/{key}/latest/{basecurrency}");

        //create real instance service
        ExchangeRateUpdaterService realService = new ExchangeRateUpdaterService(repository,properties);

        //spy the service to mock api method call
        ExchangeRateUpdaterService serviceSpy = spy(realService);

        //Mock API response
        Map<String,Double> rates = new HashMap<>();
        rates.put("EUR",0.92);
        rates.put("GBP",0.8);

        Map<String , Object> apiResponse = Map.of("conversion_rates",rates);

        doReturn(apiResponse).when(serviceSpy)
                .callExternalApi(anyString());

        serviceSpy.updateRatesForBaseCurrency("USD");

        verify(repository,atLeastOnce()).save(any());

    }
}
