package org.example.repository;

import org.example.domain.CourierCode;
import org.example.domain.PackageSize;
import org.example.domain.ShippingPrice;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShippingPriceRepositoryTest {
    private final ShippingPriceRepository sut = new ShippingPriceRepository();

    @Test
    void shouldGetEmptyWhenNotFindShippingPrice() {
        var actual = sut.getShippingPrice(CourierCode.LP, PackageSize.L);

        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldGetShippingPrice() {
        sut.addPriceList(List.of(
                new ShippingPrice("LP S 1.50"),
                new ShippingPrice("LP M 4.90"),
                new ShippingPrice("LP L 6.90"),
                new ShippingPrice("MR S 2"),
                new ShippingPrice("MR M 3"),
                new ShippingPrice("MR L 4")
        ));

        var actual = sut.getShippingPrice(CourierCode.LP, PackageSize.L);
        assertEquals(actual.orElseThrow(), new BigDecimal("6.90"));
    }

    @Test
    void shouldGetMaxDiffShippingPrice() {
        sut.addPriceList(List.of(
                new ShippingPrice("LP S 1.50"),
                new ShippingPrice("LP M 4.90"),
                new ShippingPrice("LP L 6.90"),
                new ShippingPrice("MR S 2"),
                new ShippingPrice("MR M 3"),
                new ShippingPrice("MR L 4")
        ));

        var actual1 = sut.getMaxDiffShippingPrice(PackageSize.S);
        assertEquals(actual1.orElseThrow(), new BigDecimal("0.50"));

        var actual2 = sut.getMaxDiffShippingPrice(PackageSize.M);
        assertEquals(actual2.orElseThrow(), new BigDecimal("1.90"));

        var actual3 = sut.getMaxDiffShippingPrice(PackageSize.L);
        assertEquals(actual3.orElseThrow(), new BigDecimal("2.90"));
    }

    @Test
    void shouldGetLowestShippingPrice() {
        sut.addPriceList(List.of(
                new ShippingPrice("LP S 1.50"),
                new ShippingPrice("LP M 4.90"),
                new ShippingPrice("LP L 6.90"),
                new ShippingPrice("MR S 2"),
                new ShippingPrice("MR M 3"),
                new ShippingPrice("MR L 4")
        ));

        var actual1 = sut.getLowestShippingPrice(PackageSize.S);
        assertEquals(actual1.orElseThrow(), new BigDecimal("1.50"));

        var actual2 = sut.getLowestShippingPrice(PackageSize.M);
        assertEquals(actual2.orElseThrow(), new BigDecimal("3"));

        var actual3 = sut.getLowestShippingPrice(PackageSize.L);
        assertEquals(actual3.orElseThrow(), new BigDecimal("4"));
    }
}