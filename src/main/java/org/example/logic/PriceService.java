package org.example.logic;

import org.example.domain.Transaction;
import org.example.logic.rules.PriceRule;
import org.example.logic.rules.SmallShipmentDiscountRule;
import org.example.logic.rules.StandardPriceRule;
import org.example.logic.rules.ThirdLShipmentInMonthViaLPRule;
import org.example.repository.AccumulatedDiscountRepository;
import org.example.repository.ShippingPriceRepository;

import java.util.List;

public class PriceService {

    private final StandardPriceRule standardPriceRule;
    private final List<PriceRule> discountRules;

    public PriceService(ShippingPriceRepository shippingPriceRepository, AccumulatedDiscountRepository accumulatedDiscountRepository) {
        standardPriceRule = new StandardPriceRule(shippingPriceRepository);
        discountRules = List.of(
                new SmallShipmentDiscountRule(shippingPriceRepository, accumulatedDiscountRepository),
                new ThirdLShipmentInMonthViaLPRule(accumulatedDiscountRepository)
        );
    }

    public void apply(List<Transaction> transactions) {
        applyStandardPrice(transactions);
        applyDiscount(transactions);
    }

    private void applyStandardPrice(List<Transaction> transactions) {
        transactions.forEach(standardPriceRule::apply);
    }

    private void applyDiscount(List<Transaction> transactions) {
        transactions.forEach(tx -> discountRules.forEach(it -> it.apply(tx)));
    }
}
