package org.example.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Transaction {
    private static final Pattern transactionRecordPattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2}) ([A-Z]+) ([A-Z]+)$");

    private final String originalValue;

    private LocalDate transactionDate;
    private PackageSize packageSize;
    private CourierCode courierCode;
    @Setter
    private boolean isValid;
    @Setter
    private BigDecimal shipmentPrice = BigDecimal.ZERO;
    private BigDecimal shipmentDiscount = BigDecimal.ZERO;

    public Transaction(String originalValue) {
        this.originalValue = originalValue;
        parse(originalValue);
    }

    private void parse(String value) {
        Matcher matcher = transactionRecordPattern.matcher(value);
        if (matcher.find() && matcher.groupCount() == 3) {
            try {
                transactionDate = LocalDate.parse(matcher.group(1));
                packageSize = PackageSize.valueOf(matcher.group(2));
                courierCode = CourierCode.valueOf(matcher.group(3));

                isValid = true;
            } catch (Exception e) {
                isValid = false;
            }
        }
    }

    public BigDecimal applyDiscount(BigDecimal discount) {
        if (discount.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        var discountToApply = shipmentPrice.min(discount);

        shipmentPrice = shipmentPrice.subtract(discountToApply);
        shipmentDiscount = shipmentDiscount.add(discountToApply);

        return discountToApply;
    }

    public String getFormattedValue() {
        if (isValid) {
            if (getShipmentDiscount().compareTo(BigDecimal.ZERO) > 0) {
                return String.format(Locale.ENGLISH, "%s %,.2f %,.2f", getOriginalValue(), getShipmentPrice(), getShipmentDiscount());
            } else {
                return String.format(Locale.ENGLISH, "%s %,.2f -", getOriginalValue(), getShipmentPrice());
            }
        } else {
            return String.format("%s Ignored", getOriginalValue());
        }
    }
}
