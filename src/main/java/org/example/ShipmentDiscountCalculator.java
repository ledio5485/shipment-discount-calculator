package org.example;

import org.example.logic.FileInputParser;
import org.example.logic.PriceService;
import org.example.logic.ConsolePrintService;
import org.example.repository.AccumulatedDiscountRepository;
import org.example.repository.ShippingPriceRepository;

public class ShipmentDiscountCalculator {

    public static void main(String[] args) {
        var transactions = FileInputParser.parseTransactionList("input.txt");
        var shippingPriceList = FileInputParser.parseShippingPrice("price.txt");

        var shippingPriceRepository = new ShippingPriceRepository();
        shippingPriceRepository.addPriceList(shippingPriceList);
        var accumulatedDiscountRepository = new AccumulatedDiscountRepository();

        var priceService = new PriceService(shippingPriceRepository, accumulatedDiscountRepository);
        priceService.apply(transactions);

        var printService = new ConsolePrintService();
        printService.print(transactions);
    }
}
