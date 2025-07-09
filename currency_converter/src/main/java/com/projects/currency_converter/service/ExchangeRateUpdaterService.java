package com.projects.currency_converter.service;

import com.projects.currency_converter.configuration.ExchangeApiProperties;
import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.repository.ExchangeRateRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateUpdaterService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateUpdaterService.class);

    final private ExchangeRateRepository repository;

    final private ExchangeApiProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void initializeRates() {
        logger.info("initializeRates() :: Starting to initialize exchange rates with USD as default.....");
        updateRatesForBaseCurrency("USD");
        logger.info("initializeRates() :: Finished initializing exchange rates !!!!");
    }

    @Deprecated
    public void updateRates(){
        updateRatesForBaseCurrency("USD");
    }

    public void updateRatesForBaseCurrency(String   baseCurrency){
        try{
            logger.info("updateRatesForBaseCurrency() :: Fetching rates from API.....");
            String baseUrl = properties.getUrl();
            String apiUrl = baseUrl.replace("{key}", properties.getKey())
                    .replace("{basecurrency}", baseCurrency);

            logger.info("updateRatesForBaseCurrency() :: API URL: {}" , apiUrl);

            Map response = callExternalApi(apiUrl);

            logger.info("updateRatesForBaseCurrency() :: Response received {}: {}",baseCurrency,response);

            Map<String , Double> rates = (Map<String, Double>) response.get("conversion_rates");

            logger.info("updateRatesForBaseCurrency() :: Rates received {} :: {} ",baseCurrency,rates);

            if(rates != null){
                logger.info("Processing Rates for base currency: {}",baseCurrency);

                for(Map.Entry<String,Double> entry : rates.entrySet()){
                    String toCurrency = entry.getKey();

                    Number rateNumber = (Number) entry.getValue();
                    double rate = rateNumber.doubleValue();

                    if(!baseCurrency.equals(toCurrency)){
                        String pair = baseCurrency + "_" + toCurrency;
                        logger.info("PAIR : {}" , pair);

                        boolean rateExists = repository.findByFromCurrencyAndToCurrency(baseCurrency,toCurrency)
                                .filter(existingRate -> existingRate.getLocalDateTime().equals(LocalDate.now())).isPresent();

                        if(!rateExists){
                            ExchangeRate exchangeRate = new ExchangeRate();
                            exchangeRate.setCurrencyPair(pair);
                            exchangeRate.setFromCurrency(baseCurrency);
                            exchangeRate.setToCurrency(toCurrency);
                            exchangeRate.setRate(BigDecimal.valueOf(rate));
                            exchangeRate.setLocalDateTime(LocalDate.now());

                            repository.save(exchangeRate);

                            logger.info("updateRatesForBaseCurrency() :: Rates saved for pair : {}",pair);
                        }else{
                            logger.info("updateRatesForBaseCurrency() :: Rate already exists for the pair : {} for today",pair);
                        }
                    }
                }

                logger.info("updateRatesForBaseCurrency() :: Number of successful rates saved : {}", rates.size());
            }

        }catch (Exception e){
            logger.error("updateRatesForBaseCurrency() :: Exception occurred for base currency {} :" , baseCurrency);
        }
    }

    //method to allow spying/mocking in tests
    protected  Map callExternalApi(String apiUrl){
        return restTemplate.getForObject(apiUrl,Map.class);
    }
}