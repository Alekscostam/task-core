package com.rates.rates.service;

import com.rates.rates.TableType;
import com.rates.rates.dao.RateRepository;
import com.rates.rates.entity.Rate;
import com.rates.rates.entity.projection.CalculateCostRequest;
import com.rates.rates.entity.projection.CalculatedCostResponse;
import com.rates.rates.entity.projection.RateResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RateService {


    private final RateRepository repository;

    public RateService(RateRepository repository) {
        this.repository = repository;
    }

    private static LocalDate getNotNullLocalDate(LocalDate date){
        return Optional.ofNullable(date).orElse(LocalDate.now());
    }

    public RateResponse getSalesRatesByDateAndCurrency(LocalDate date, String currency){
        date = getAdjustedDate(getNotNullLocalDate(date));
        Rate rate = repository.findByDateAndCode(date, currency, TableType.A).orElseThrow(() -> new IllegalStateException("Nie znaleizono kursu"));
        return new RateResponse(rate.getMid(),rate.getCode(), rate.getExchangeRate().getEffectiveDate());
    }

    public CalculatedCostResponse calculateTotalCostInPLN(List<String> currencyCodes, LocalDate date) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (String currencyCode : currencyCodes) {
            Rate rate = repository.findByDateAndCode(date, currencyCode, TableType.C)
                    .orElseThrow(() -> new IllegalStateException("Nie znaleziono kursu dla waluty: " + currencyCode));
            BigDecimal averageRate = rate.getBid().add(rate.getAsk()).divide(BigDecimal.valueOf(2), 4, BigDecimal.ROUND_HALF_UP);
            totalCost = totalCost.add(averageRate);
        }
        return new CalculatedCostResponse(totalCost);
    }



    /** NBP aktualizuje daty około 12:30(ta informacja jest z 01.2025), więc jeśli bedzie przed 12 50 to pobierze z DB kurs z wczoraj, żeby nie poleciał LogicalRuntimeException */
    private  LocalDate getAdjustedDate(LocalDate date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        date = backToWorkingDay(date);
        if (today.isEqual(date)) {
            LocalDateTime today1250 = now.with(LocalTime.of(12, 50));
            if (now.isBefore(today1250)) {
                date = date.minusDays(1);
                date = backToWorkingDay(date);
            }
        }
        return date;
    }
    private LocalDate backToWorkingDay(LocalDate localDate){
        while (isWeekend(localDate)) {
            if (localDate.getDayOfWeek() == DayOfWeek.SATURDAY) {
                localDate = localDate.minusDays(1);
            } else if (localDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                localDate = localDate.minusDays(2);
            }
        }
        return localDate;
    }
    private static boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY ;
    }



}
