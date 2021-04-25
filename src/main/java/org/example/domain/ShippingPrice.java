package org.example.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ShippingPrice {
    private static final Pattern shippingPriceRecordPattern = Pattern.compile("^([A-Z]+) ([A-Z]+) (\\d+(\\.\\d{1,2})?){1}$");

    private CourierCode courierCode;
    private PackageSize packageSize;
    private BigDecimal shippingPrice;

    private boolean isValid;

    public ShippingPrice(String value) {
        parse(value);
    }

    private void parse(String value) {
        Matcher matcher = shippingPriceRecordPattern.matcher(value);
        if (matcher.find() && matcher.groupCount() == 4) {
            try {
                courierCode = CourierCode.valueOf(matcher.group(1));
                packageSize = PackageSize.valueOf(matcher.group(2));
                shippingPrice = new BigDecimal(matcher.group(3));
                isValid = true;
            } catch (Exception e) {
                isValid = false;
            }
        }
    }
}
