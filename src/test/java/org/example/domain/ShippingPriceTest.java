package org.example.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ShippingPriceTest {

    @Test
    void shouldParseCorrectly() {
        var actual = new ShippingPrice("LP S 1.50");

        assertTrue(actual.isValid());
        assertEquals(actual.getCourierCode(), CourierCode.LP);
        assertEquals(actual.getPackageSize(), PackageSize.S);
        assertEquals(actual.getShippingPrice(), new BigDecimal("1.50"));
    }

    @Test
    void shouldMakeInvalidIfCouldNotParse() {
        var actual = new ShippingPrice("LP XXL 1.50");

        assertFalse(actual.isValid());
    }
}