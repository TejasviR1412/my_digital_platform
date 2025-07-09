package com.projects.currency_converter.controller;

import com.projects.currency_converter.model.ExchangeRate;
import com.projects.currency_converter.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
@Tag(name = "Currency Conversion" , description = "API for currency conversion operations")
public class CurrencyApiContoller {
    private final CurrencyService currencyService;

    @Operation(
            summary = "Convert currency amount",
            description = "Convert an amount from one currency to another using current exchange rates"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" , description = "Currency conversion successful"),
            @ApiResponse(responseCode = "400" , description = "Invalid currency codes or amount"),
            @ApiResponse(responseCode = "500" , description = "Exchange rate not found or Service Error")
    })
    @PostMapping("/convert")
    public ResponseEntity<Double> convert(
            @Parameter(description = "Source Currency Code (E.g. , USD , EUR)",required = true)
            @RequestParam String from,

            @Parameter(description = "Target Currency Code (E.g. , USD , EUR)",required = true)
            @RequestParam String to,

            @Parameter(description = "Amount to convert" , required = true)
            @RequestParam double amount) {
        try{
            double convertedAmount = currencyService.convert(from,to,amount);
            return ResponseEntity.ok(convertedAmount);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get all exchange rates",
            description = "Retrieve all available exchange rates stored in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Retrieved exchange rates successfully"),
            @ApiResponse(responseCode = "500" , description = "Internal server error")
    })
    @GetMapping("/rates")
    public ResponseEntity<List<ExchangeRate>> getAllRates(){
        try{
            List<ExchangeRate> rates = currencyService.getAllRates();
            return ResponseEntity.ok(rates);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Get exchange rates for a specific base currency",
            description = "Retrieve all exchange rates for a specific base currency"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200" ,description = "Retrieved exchange rate for a specifc currency successfully"),
            @ApiResponse(responseCode = "404" , description = "Currency not found"),
            @ApiResponse(responseCode = "500" , description = "Internal server error")
    })
    @GetMapping("/rates/{baseCurrency}")
    public ResponseEntity<List<ExchangeRate>> getRatesForCurrency(
            @Parameter(description = "Base currency code (E.g. , USD , EUR)" , required = true)
            @PathVariable String baseCurrency
    ){
        try{
            List<ExchangeRate> rates = currencyService.getAllRates()
                    .stream()
                    .filter(rate -> rate.getFromCurrency().equals(baseCurrency.toUpperCase()))
                    .toList();

            if(rates.isEmpty()){
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(rates);
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }
}