package org.example.logic.rules;

import org.example.domain.Transaction;
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
class StandardPriceRuleTest {

    @InjectMocks
    private StandardPriceRule sut;

    @Mock
    private ShippingPriceRepository repository;

    @Test
    void shouldNotApplyStandardPriceIfTransactionIsInvalid() {
        var tx = new Transaction("2015-02-29 CUSPS");

        sut.apply(tx);

        assertFalse(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.ZERO);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyStandardPriceWhenThePriceIsNotFound() {
        var tx = new Transaction("2015-02-01 S MR");
        Mockito.when(repository.getShippingPrice(tx.getCourierCode(), tx.getPackageSize()))
                .thenReturn(Optional.empty());

        sut.apply(tx);

        assertFalse(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.ZERO);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldApplyStandardPrice() {
        var tx = new Transaction("2015-02-01 S MR");
        Mockito.when(repository.getShippingPrice(tx.getCourierCode(), tx.getPackageSize()))
                .thenReturn(Optional.of(BigDecimal.TEN));

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }
}