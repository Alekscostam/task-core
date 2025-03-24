package com.rates.rates.integration.nbp.config;

import com.google.gson.Gson;
import com.rates.rates.integration.nbp.NbpApi;
import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NbpConfig {

    // Konstruktor nie jest potrzebny, ponieważ @Bean załatwia inicjalizację

    @Bean
    public NbpApi nbpApi() {
        String value = "https://api.nbp.pl/api";
        return Feign.builder()
                .encoder(new GsonEncoder(new Gson()))
                .decoder(new GsonDecoder(new Gson()))
                .logLevel(Logger.Level.FULL)
                .target(NbpApi.class, value);
    }
}
