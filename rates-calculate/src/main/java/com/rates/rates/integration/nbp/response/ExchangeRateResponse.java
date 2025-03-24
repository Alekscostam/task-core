package com.rates.rates.integration.nbp.response;

import com.rates.rates.TableType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateResponse {
    private String effectiveDate;
    private String no;
    private TableType table; // moze byc enum, ale moze lepiej nie
    private List<RateResponse> rates;


}
