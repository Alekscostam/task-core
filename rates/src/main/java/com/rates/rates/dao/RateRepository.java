package com.rates.rates.dao;

import com.rates.rates.TableType;
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
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select r from Rate r left join fetch r.exchangeRate er where er.effectiveDate = :effectiveDate and r.code = :code and er.type = :type ")
    Optional<Rate> findByDateAndCode(@Param("effectiveDate") LocalDate effectiveDate, @Param("code") String code, @Param("type") TableType type);


}
