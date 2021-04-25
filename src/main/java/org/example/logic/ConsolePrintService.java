package org.example.logic;

import org.example.domain.Transaction;

import java.util.List;

public class ConsolePrintService {

    public void print(List<Transaction> transactions) {
        transactions.forEach(tx -> System.out.println(tx.getFormattedValue()));
    }
}
