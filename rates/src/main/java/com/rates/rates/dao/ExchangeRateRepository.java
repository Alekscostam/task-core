package com.rates.rates.dao;

import com.rates.rates.entity.ExchangeRate;
import com.rates.rates.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@Transactional
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

}
