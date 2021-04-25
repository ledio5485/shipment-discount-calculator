package org.example.logic;

import org.example.domain.ShippingPrice;
import org.example.domain.Transaction;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileInputParser {

    public static List<Transaction> parseTransactionList(String inputFilename) {
        return parse(inputFilename, Transaction::new);
    }

    public static List<ShippingPrice> parseShippingPrice(String shippingPriceFilename) {
        return parse(shippingPriceFilename, ShippingPrice::new);
    }

    private static <T> List<T> parse(String filename, Function<? super String, T> mapper) {
        try {
            return Files.lines(Paths.get(filename))
                    .map(mapper)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse the file " + filename, e);
        }
    }
}
