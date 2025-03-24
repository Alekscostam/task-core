package com.rates.rates.entity;

import com.rates.rates.TableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "exchange_rate")
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private LocalDate effectiveDate;

    private String no;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false)
    private TableType type;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,  mappedBy = "exchangeRate")
    private Set<Rate> rates = new HashSet<>();

    public ExchangeRate(LocalDate effectiveDate, String no, TableType type, Set<Rate> rates) {
        this.effectiveDate = effectiveDate;
        this.no = no;
        this.type = type;
        this.rates = rates;
    }
}
