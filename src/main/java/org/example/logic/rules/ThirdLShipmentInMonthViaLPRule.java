package org.example.logic.rules;

import lombok.AllArgsConstructor;
import org.example.domain.CourierCode;
import org.example.domain.PackageSize;
import org.example.domain.Transaction;
import org.example.repository.AccumulatedDiscountRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class ThirdLShipmentInMonthViaLPRule implements PriceRule {

    private final Map<YearMonth, AtomicInteger> shipmentCounter = new ConcurrentHashMap<>();
    private final AccumulatedDiscountRepository accumulatedDiscountRepository;

    public void apply(Transaction tx) {
        if (!tx.isValid() || !(tx.getPackageSize().equals(PackageSize.L) && tx.getCourierCode().equals(CourierCode.LP))) {
            return;
        }

        int counter = getCounter(YearMonth.from(tx.getTransactionDate()));
        if (counter == 3) {
            var availableDiscount = accumulatedDiscountRepository.getAvailableDiscount(tx.getTransactionDate());
            if (availableDiscount.compareTo(BigDecimal.ZERO) > 0) {
                var appliedDiscount = tx.applyDiscount(availableDiscount);
                accumulatedDiscountRepository.subtractFromAvailableDiscount(appliedDiscount, tx.getTransactionDate());
            }
        }
    }

    private int getCounter(YearMonth yearMonth) {
        shipmentCounter.putIfAbsent(yearMonth, new AtomicInteger(0));
        return shipmentCounter.get(yearMonth).incrementAndGet();
    }
}
