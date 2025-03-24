package com.rates.rates.entity.projection;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CalculateCostRequest {
    private List<String> currencyCodes;  // Lista kodów walut, np. ["USD", "EUR", "GBP"]
    private String date;                 // Data w formacie "yyyy-MM-dd", np. "2025-03-24"

    // Getters and Setters
    public List<String> getCurrencyCodes() {
        return currencyCodes;
    }

    public void setCurrencyCodes(List<String> currencyCodes) {
        this.currencyCodes = currencyCodes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
