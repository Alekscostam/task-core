package com.rates.rates.task;

import com.rates.rates.integration.nbp.ExchangeRateService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
//@Slf4j
//@RequiredArgsConstructor
public class ExchangeRateTask {

    private final ExchangeRateService exchangeRateService;

    public ExchangeRateTask(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationStart() {
        reinitializeRates();
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void taskLogic(){
        reinitializeRates();
    }

    private void reinitializeRates() {
//        log.info("Start update exchange rates.");
        exchangeRateService.createAndSaveSalesRates();
        exchangeRateService.createAndSaveCostOfPurchasesRates();
//        log.info("End update exchange rates.");
    }

}
