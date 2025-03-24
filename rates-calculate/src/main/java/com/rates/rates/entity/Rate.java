package com.rates.rates.entity;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Rate")
@NoArgsConstructor
@AllArgsConstructor
public class Rate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 4)
    private String code;

    @Column(name = "currency", nullable = false, length = 50) // to tak naprawde curency opisowy, a nie skrót
    private String currency;

    @Column(name = "mid", precision = 18, scale = 2)
    private BigDecimal mid;

    @Column(name = "bid", precision = 18, scale = 2)
    private BigDecimal bid;

    @Column(name = "ask", precision = 18, scale = 2)
    private BigDecimal ask;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "exchange_rate_id", nullable = false)
    private ExchangeRate exchangeRate;


    public Rate(String code, String currency, BigDecimal mid,BigDecimal bid,BigDecimal ask, ExchangeRate exchangeRate) {
        this.code = code;
        this.currency = currency;
        this.mid = mid;
        this.ask = ask;
        this.bid = bid;
        this.exchangeRate = exchangeRate;
    }
}
