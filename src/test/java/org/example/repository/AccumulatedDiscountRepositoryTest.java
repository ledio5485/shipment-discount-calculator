package org.example.repository;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AccumulatedDiscountRepositoryTest {

    @Test
    void shouldGetDefaultMaxDiscountWhenNotFoundForCurrentYearAndMonth() {
        var sut = new AccumulatedDiscountRepository(BigDecimal.TEN);

        var actual = sut.getAvailableDiscount(LocalDate.now(Clock.systemUTC()));

        assertEquals(actual, BigDecimal.TEN);
    }

    @Test
    void shouldGetAvailableDiscountWhenFoundForCurrentYearAndMonth() {
        var sut = new AccumulatedDiscountRepository(BigDecimal.TEN);
        sut.subtractFromAvailableDiscount(BigDecimal.ONE, LocalDate.now(Clock.systemUTC()));

        var actual = sut.getAvailableDiscount(LocalDate.now(Clock.systemUTC()));

        assertEquals(actual, new BigDecimal("9"));
    }

    @Test
    void shouldSubtractFromAvailableDiscount() {
        var sut = new AccumulatedDiscountRepository(BigDecimal.TEN);

        var amountSubtracted = sut.subtractFromAvailableDiscount(BigDecimal.ONE, LocalDate.now(Clock.systemUTC()));

        assertEquals(amountSubtracted, BigDecimal.ONE);

        var availableDiscount = sut.getAvailableDiscount(LocalDate.now(Clock.systemUTC()));

        assertEquals(availableDiscount, new BigDecimal("9"));
    }

    @Test
    void shouldNotSubtractIfTheAvailableDiscountBecomeNegative() {
        var sut = new AccumulatedDiscountRepository(BigDecimal.ONE);

        var amountSubtracted = sut.subtractFromAvailableDiscount(BigDecimal.ONE, LocalDate.now(Clock.systemUTC()));

        assertEquals(amountSubtracted, BigDecimal.ONE);

        var amountSubtracted2 = sut.subtractFromAvailableDiscount(BigDecimal.ONE, LocalDate.now(Clock.systemUTC()));

        assertEquals(amountSubtracted2, BigDecimal.ZERO);

        var actual = sut.getAvailableDiscount(LocalDate.now(Clock.systemUTC()));

        assertEquals(actual, BigDecimal.ZERO);
    }
}