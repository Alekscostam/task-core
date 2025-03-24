package com.rates.rates.integration.nbp;


import com.rates.rates.integration.nbp.response.ExchangeRateResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.time.LocalDate;
import java.util.List;

public interface NbpApi {

    @RequestLine("GET /exchangerates/tables/{table}/?format=json")
    @Headers({"Content-Type: application/json"})
    List<ExchangeRateResponse> getTableExchangeRates(@Param("table") String table);

    @RequestLine("GET /exchangerates/tables/{table}/{startDate}/{endDate}?format=json")
    @Headers({"Content-Type: application/json"})
    List<ExchangeRateResponse> getTableExchangeRates(@Param("table") String table, @Param("startDate")LocalDate startDate, @Param("endDate") LocalDate endDate);


    @RequestLine("GET /exchangerates/rates/{table}/{code}/{date}/?format=json")
    @Headers({"Content-Type: application/json"})
    ExchangeRateResponse getTableExchangeRate(@Param("table") String table, @Param("code") String code, @Param("date") String date);


}
