package org.example.logic.rules;

import lombok.AllArgsConstructor;
import org.example.domain.PackageSize;
import org.example.domain.Transaction;
import org.example.repository.AccumulatedDiscountRepository;
import org.example.repository.ShippingPriceRepository;

import java.math.BigDecimal;

@AllArgsConstructor
public class SmallShipmentDiscountRule implements PriceRule {

    private final ShippingPriceRepository shippingPriceRepository;
    private final AccumulatedDiscountRepository accumulatedDiscountRepository;

    public void apply(Transaction tx) {
        if (!tx.isValid() || !tx.getPackageSize().equals(PackageSize.S)) {
            return;
        }

        var lowestShippingPrice = shippingPriceRepository.getLowestShippingPrice(tx.getPackageSize());
        if (lowestShippingPrice.isPresent() && lowestShippingPrice.get().compareTo(tx.getShipmentPrice()) == 0) {
            return;
        }

        var availableDiscount = accumulatedDiscountRepository.getAvailableDiscount(tx.getTransactionDate());
        var maxDiffShippingPrice = shippingPriceRepository.getMaxDiffShippingPrice(tx.getPackageSize());
        if (availableDiscount.compareTo(BigDecimal.ZERO) > 0 &&
                maxDiffShippingPrice.isPresent() &&
                maxDiffShippingPrice.get().compareTo(BigDecimal.ZERO) > 0) {
            var discountToApply = availableDiscount.min(maxDiffShippingPrice.get()).min(tx.getShipmentPrice());
            tx.applyDiscount(discountToApply);
            accumulatedDiscountRepository.subtractFromAvailableDiscount(discountToApply, tx.getTransactionDate());
        }
    }
}
