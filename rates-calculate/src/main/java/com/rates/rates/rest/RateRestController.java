package com.rates.rates.rest;


import com.rates.rates.entity.projection.CalculateCostRequest;
import com.rates.rates.entity.projection.CalculatedCostResponse;
import com.rates.rates.entity.projection.RateResponse;
import com.rates.rates.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/exchangeRate")
@RestController
@Validated
public class RateRestController {

    private final RateService service;


    @GetMapping("/")
    public ResponseEntity<RateResponse> getSales(
            @RequestParam(value = "code", required = true)  String code,
            @RequestParam(value = "date", required = true)  String date
            ) {
        return new ResponseEntity<>(service.getSalesRatesByDateAndCurrency(LocalDate.parse(date), code), HttpStatus.OK);
    }


    @PostMapping("/calculate-cost")
    public ResponseEntity<CalculatedCostResponse> calculateTotalCost(@RequestBody CalculateCostRequest request) {
        CalculatedCostResponse calculatedCostResponse = service.calculateTotalCostInPLN(request.getCurrencyCodes(), LocalDate.parse(request.getDate()));
        return new ResponseEntity<>(calculatedCostResponse, HttpStatus.OK);
    }



}
