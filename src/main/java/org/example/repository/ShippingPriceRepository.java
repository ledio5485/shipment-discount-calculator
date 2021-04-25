package org.example.repository;

import org.example.domain.CourierCode;
import org.example.domain.PackageSize;
import org.example.domain.ShippingPrice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class ShippingPriceRepository {
    private final Collection<ShippingPrice> shippingPriceList = new ArrayList<>();

    public void addPriceList(Collection<ShippingPrice> priceList) {
        priceList.stream()
                .filter(ShippingPrice::isValid)
                .forEach(shippingPriceList::add);
    }

    public Optional<BigDecimal> getShippingPrice(CourierCode courierCode, PackageSize packageSize) {
        return shippingPriceList.stream()
                .filter(it -> it.getCourierCode().equals(courierCode) && it.getPackageSize().equals(packageSize))
                .map(ShippingPrice::getShippingPrice)
                .findFirst();
    }

    public Optional<BigDecimal> getMaxDiffShippingPrice(PackageSize packageSize) {
        var lowest = getLowestShippingPrice(packageSize);
        var highest = getHighestShippingPrice(packageSize);

        return lowest.isPresent() && highest.isPresent() ? Optional.of(highest.get().subtract(lowest.get())) : Optional.empty();
    }

    public Optional<BigDecimal> getLowestShippingPrice(PackageSize packageSize) {
        return shippingPriceList.stream()
                .filter(it -> it.getPackageSize().equals(packageSize))
                .min(Comparator.comparing(ShippingPrice::getShippingPrice))
                .map(ShippingPrice::getShippingPrice);
    }

    private Optional<BigDecimal> getHighestShippingPrice(PackageSize packageSize) {
        return shippingPriceList.stream()
                .filter(it -> it.getPackageSize().equals(packageSize))
                .max(Comparator.comparing(ShippingPrice::getShippingPrice))
                .map(ShippingPrice::getShippingPrice);
    }
}
