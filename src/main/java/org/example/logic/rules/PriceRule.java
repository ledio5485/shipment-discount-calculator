package org.example.logic.rules;

import org.example.domain.Transaction;

public interface PriceRule {
    void apply(Transaction tx);
}
