package com.rates.rates.integration.nbp;

import com.rates.rates.TableType;
import com.rates.rates.dao.ExchangeRateRepository;
import com.rates.rates.dao.RateRepository;
import com.rates.rates.entity.ExchangeRate;
import com.rates.rates.entity.Rate;
import com.rates.rates.integration.nbp.response.ExchangeRateResponse;
import com.rates.rates.integration.nbp.response.RateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ExchangeRateService {
    private final ExchangeRateRepository repository;
    private final NbpApi nbpApi;

    public ExchangeRateService(ExchangeRateRepository repository, NbpApi nbpApi) {
        this.repository = repository;
        this.nbpApi = nbpApi;
    }

    //    private final ExchangeRateRepository repository;

    public void createAndSaveSalesRates(){
        LocalDate startDate = LocalDate.now().minusDays(80);
        LocalDate endDate = LocalDate.now();
        Set<ExchangeRateResponse> exchanges = nbpApi.getTableExchangeRates(TableType.A.name(), startDate, endDate).stream().collect(Collectors.toSet());
        List<ExchangeRate> exchangeRates = mapRatesToEntity(exchanges);
        repository.saveAll(exchangeRates);
    }
    public void createAndSaveCostOfPurchasesRates(){
        LocalDate startDate = LocalDate.now().minusDays(80);
        LocalDate endDate = LocalDate.now();
        Set<ExchangeRateResponse> exchanges = nbpApi.getTableExchangeRates(TableType.C.name(), startDate, endDate).stream().collect(Collectors.toSet());
        List<ExchangeRate> exchangeRates = mapRatesToEntity(exchanges);
        repository.saveAll(exchangeRates);
    }

    private static List<ExchangeRate> mapRatesToEntity(Set<ExchangeRateResponse> exchanges) {
        List<ExchangeRate> exchangeRates = exchanges.stream().map(exchangeRateResponse ->
                {
                    ExchangeRate exchangeRate = new ExchangeRate();
                    exchangeRate.setNo(exchangeRateResponse.getNo());
                    exchangeRate.setEffectiveDate(LocalDate.parse(exchangeRateResponse.getEffectiveDate()));
                    exchangeRate.setType(exchangeRateResponse.getTable());
                    Set<Rate> rates = exchangeRateResponse.getRates().stream().map(r -> {
                        Rate rate = new Rate();
                        rate.setExchangeRate(exchangeRate);
                        rate.setCode(r.getCode());
                        rate.setMid(r.getMid());
                        rate.setCurrency(r.getCurrency());
                        rate.setBid(r.getBid());
                        rate.setAsk(r.getAsk());
                        return rate;
                    }).collect(Collectors.toSet());
                    return new ExchangeRate(
                            LocalDate.parse(exchangeRateResponse.getEffectiveDate()),
                            exchangeRateResponse.getNo(),
                            exchangeRateResponse.getTable(),
                            rates
                    );
                }

        ).collect(Collectors.toList());
        return exchangeRates;
    }


//    public ExchangeRate getByDateAndCurrency(LocalDate date, Currency currency){
////        date = getAdjustedDate(getNotNullLocalDate(date));
////        Optional<ExchangeRate> exchangeRateOptional = repository.findByDateAndCode(date, currency.name());
////        if(exchangeRateOptional.isPresent()){
////            return exchangeRateOptional.get();
////        }
////        Rate rate = nbpApi.getTableExchangeRate(TableNbpType.A.name(), currency.name(),validDateForNbpAPi(date)).getRates().getFirst();
////        return new ExchangeRate(date, currency.name(), rate.getCurrency(), rate.getMid());
//    }




    /** NBP aktualizuje daty około 12:30(ta informacja jest z 01.2025), więc jeśli bedzie przed 12 50 to pobierze z DB kurs z wczoraj, żeby nie poleciał LogicalRuntimeException */
    private  LocalDate getAdjustedDate(LocalDate date) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDate today = now.toLocalDate();
//        date = backToWorkingDay(date);
//        if (today.isEqual(date)) {
//            LocalDateTime today1250 = now.with(LocalTime.of(12, 50));
//            if (now.isBefore(today1250)) {
//                date = date.minusDays(1);
//                date = backToWorkingDay(date);
//            }
//        }
        return date;
    }


    private static boolean isWeekendOrHoliday(LocalDate date, Set<LocalDate> holidaysDates) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY ||
                holidaysDates.contains(date);
    }

    private String validDateForNbpAPi(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    private static LocalDate getNotNullLocalDate(LocalDate date){
        return Optional.ofNullable(date).orElse(LocalDate.now());
    }

}

