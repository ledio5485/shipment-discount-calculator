package org.example.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void shouldNotParseWhenIsNotValid() {
        var actual = new Transaction("2015-02-29 S MR");

        assertFalse(actual.isValid());
    }

    @Test
    void shouldParseCorrectly() {
        var actual = new Transaction("2015-02-01 S MR");

        assertTrue(actual.isValid());
        assertEquals(actual.getTransactionDate(), LocalDate.of(2015, 2, 1));
        assertEquals(actual.getPackageSize(), PackageSize.S);
        assertEquals(actual.getCourierCode(), CourierCode.MR);
    }

    @Test
    void shouldNotApplyDiscountWhenItIsNegative() {
        var tx = new Transaction("2015-02-01 S MR");
        tx.setShipmentPrice(BigDecimal.TEN);

        var discountApplied = tx.applyDiscount(BigDecimal.ONE.negate());

        assertEquals(discountApplied, BigDecimal.ZERO);
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldApplyDiscountWhenItIsLessThanShippingPrice() {
        var tx = new Transaction("2015-02-01 S MR");
        tx.setShipmentPrice(BigDecimal.TEN);

        var discountApplied = tx.applyDiscount(BigDecimal.ONE);

        assertEquals(discountApplied, BigDecimal.ONE);
        assertEquals(tx.getShipmentPrice(), new BigDecimal("9"));
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ONE);
    }

    @Test
    void shouldApplyDiscountWhenItIsBiggerThanShippingPrice() {
        var tx = new Transaction("2015-02-01 S MR");
        tx.setShipmentPrice(BigDecimal.ONE);

        var discountApplied = tx.applyDiscount(BigDecimal.TEN);

        assertEquals(discountApplied, BigDecimal.ONE);
        assertEquals(tx.getShipmentPrice(), BigDecimal.ZERO);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ONE);
    }

    @Test
    void getFormattedValueWhenItIsNotValid() {
        var originalValue = "2015-02-29 CUSPS";
        var tx = new Transaction(originalValue);

        var actual = tx.getFormattedValue();

        var expected = originalValue.concat(" Ignored");
        assertEquals(expected, actual);
    }

    @Test
    void getFormattedValueWhenDiscountIsEqualToZero() {
        var originalValue = "2015-02-01 S MR";
        var tx = new Transaction(originalValue);
        tx.setShipmentPrice(BigDecimal.ONE);

        var actual = tx.getFormattedValue();

        var expected = originalValue.concat(" 1.00 -");
        assertEquals(expected, actual);
    }

    @Test
    void getFormattedValueWhenDiscountIsGreaterThanZero() {
        var originalValue = "2015-02-01 S MR";
        var tx = new Transaction(originalValue);
        tx.setShipmentPrice(BigDecimal.TEN);
        tx.applyDiscount(BigDecimal.ONE);

        var actual = tx.getFormattedValue();

        var expected = originalValue.concat(" 9.00 1.00");
        assertEquals(expected, actual);
    }
}