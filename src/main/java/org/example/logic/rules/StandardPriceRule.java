package org.example.logic.rules;

import lombok.AllArgsConstructor;
import org.example.domain.Transaction;
import org.example.repository.ShippingPriceRepository;

@AllArgsConstructor
public class StandardPriceRule implements PriceRule {

    private final ShippingPriceRepository shippingPriceRepository;

    public void apply(Transaction tx) {
        if (!tx.isValid()) {
            return;
        }

        var shippingPrice = shippingPriceRepository.getShippingPrice(tx.getCourierCode(), tx.getPackageSize());
        if (shippingPrice.isPresent()) {
            tx.setShipmentPrice(shippingPrice.get());
        } else {
            tx.setValid(false);
        }
    }
}
