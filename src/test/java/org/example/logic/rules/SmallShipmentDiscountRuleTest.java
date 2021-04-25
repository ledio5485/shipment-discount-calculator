package org.example.logic.rules;

import org.example.domain.Transaction;
import org.example.repository.AccumulatedDiscountRepository;
import org.example.repository.ShippingPriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SmallShipmentDiscountRuleTest {

    @InjectMocks
    private SmallShipmentDiscountRule sut;

    @Mock
    private ShippingPriceRepository shippingPriceRepository;
    @Mock
    private AccumulatedDiscountRepository accumulatedDiscountRepository;

    @Test
    void shouldNotApplyWhenTransactionIsNotValid() {
        var tx = new Transaction("2015-02-29 CUSPS");

        sut.apply(tx);

        assertFalse(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.ZERO);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyWhenPackageSizeIsNotS() {
        var tx = new Transaction("2015-02-03 L LP");
        tx.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyWhenTheShippingPriceIsTheLowest() {
        var tx = new Transaction("2015-02-05 S LP");
        tx.setShipmentPrice(BigDecimal.ONE);
        Mockito.when(shippingPriceRepository.getLowestShippingPrice(tx.getPackageSize()))
                .thenReturn(Optional.of(BigDecimal.ONE));

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.ONE);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldApplyDiscount() {
        var tx = new Transaction("2015-02-05 S LP");
        tx.setShipmentPrice(BigDecimal.TEN);

        Mockito.when(shippingPriceRepository.getLowestShippingPrice(tx.getPackageSize()))
                .thenReturn(Optional.of(BigDecimal.ONE));
        Mockito.when(accumulatedDiscountRepository.getAvailableDiscount(tx.getTransactionDate()))
                .thenReturn(BigDecimal.TEN);
        Mockito.when(shippingPriceRepository.getMaxDiffShippingPrice(tx.getPackageSize()))
                .thenReturn(Optional.of(new BigDecimal("0.50")));

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), new BigDecimal("9.50"));
        assertEquals(tx.getShipmentDiscount(), new BigDecimal("0.50"));

        Mockito.verify(accumulatedDiscountRepository).subtractFromAvailableDiscount(new BigDecimal("0.50"), tx.getTransactionDate());
    }
}