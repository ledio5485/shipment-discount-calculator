package org.example.logic.rules;

import org.example.domain.Transaction;
import org.example.repository.AccumulatedDiscountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ThirdLShipmentInMonthViaLPRuleTest {

    @InjectMocks
    private ThirdLShipmentInMonthViaLPRule sut;

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
    void shouldNotApplyWhenPackageSizeIsNotL() {
        var tx = new Transaction("2015-02-05 S LP");
        tx.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyWhenCourierCodeIsNotLP() {
        var tx = new Transaction("2015-02-05 L MR");
        tx.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldNotApplyWhenNotTheThirdTimeInMonthForPackageLAndCourierCodeLP() {
        var tx = new Transaction("2015-02-05 L LP");
        tx.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx);

        assertTrue(tx.isValid());
        assertEquals(tx.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx.getShipmentDiscount(), BigDecimal.ZERO);
    }

    @Test
    void shouldApplyDiscountOnlyWhenTheThirdTimeInMonthForPackageLAndCourierCodeLP() {
        var tx1 = new Transaction("2015-02-05 L LP");
        tx1.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx1);

        assertTrue(tx1.isValid());
        assertEquals(tx1.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx1.getShipmentDiscount(), BigDecimal.ZERO);

        var tx2 = new Transaction("2015-02-06 L LP");
        tx2.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx2);

        assertTrue(tx2.isValid());
        assertEquals(tx2.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx2.getShipmentDiscount(), BigDecimal.ZERO);

        var tx3 = new Transaction("2015-02-07 L LP");
        tx3.setShipmentPrice(BigDecimal.TEN);
        Mockito.when(accumulatedDiscountRepository.getAvailableDiscount(tx3.getTransactionDate()))
                .thenReturn(new BigDecimal("100.00"));

        sut.apply(tx3);

        assertTrue(tx3.isValid());
        assertEquals(tx3.getShipmentPrice(), BigDecimal.ZERO);
        assertEquals(tx3.getShipmentDiscount(), BigDecimal.TEN);

        accumulatedDiscountRepository.subtractFromAvailableDiscount(BigDecimal.TEN, tx3.getTransactionDate());

        var tx4 = new Transaction("2015-02-08 L LP");
        tx4.setShipmentPrice(BigDecimal.TEN);

        sut.apply(tx4);

        assertTrue(tx4.isValid());
        assertEquals(tx4.getShipmentPrice(), BigDecimal.TEN);
        assertEquals(tx4.getShipmentDiscount(), BigDecimal.ZERO);
    }
}