package org.example.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccumulatedDiscountRepository {
    private static final BigDecimal DEFAULT_MAX_DISCOUNT = BigDecimal.TEN;

    private final Map<YearMonth, BigDecimal> accumulatedDiscounts = new ConcurrentHashMap<>();
    private final BigDecimal maxDiscount;

    public AccumulatedDiscountRepository(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public AccumulatedDiscountRepository() {
        this(DEFAULT_MAX_DISCOUNT);
    }

    public BigDecimal getAvailableDiscount(LocalDate localDate) {
        var yearMonth = YearMonth.from(localDate);
        accumulatedDiscounts.putIfAbsent(yearMonth, maxDiscount);

        return accumulatedDiscounts.get(yearMonth);
    }

    public BigDecimal subtractFromAvailableDiscount(BigDecimal value, LocalDate localDate) {
        var yearMonth = YearMonth.from(localDate);
        accumulatedDiscounts.putIfAbsent(yearMonth, maxDiscount);
        var amountSubtracted = value.min(accumulatedDiscounts.get(yearMonth));
        accumulatedDiscounts.computeIfPresent(yearMonth, (k, v) -> v.subtract(amountSubtracted));

        return amountSubtracted;
    }
}
