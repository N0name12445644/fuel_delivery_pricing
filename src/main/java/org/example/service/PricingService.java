
package org.example.service;

import org.example.model.ClientType;
import org.example.model.Shipment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.example.model.PricingResult;
import org.example.model.FuelType;

public class PricingService {

    private static final BigDecimal DISCOUNT_LIMIT_1 = new BigDecimal("5000");
    private static final BigDecimal DISCOUNT_LIMIT_2 = new BigDecimal("20000");

    private static final BigDecimal DISCOUNT_RATE_1 = new BigDecimal("0.05");
    private static final BigDecimal DISCOUNT_RATE_2 = new BigDecimal("0.10");

    private static final BigDecimal DELIVERY_RATE = new BigDecimal("1.80");
    private static final BigDecimal SMALL_LOAD_LIMIT = new BigDecimal("1000");
    private static final BigDecimal SMALL_LOAD_FEE = new BigDecimal("150.00");

    private static final BigDecimal GASOLINE_EXCISE = new BigDecimal("0.47");
    private static final BigDecimal DIESEL_EXCISE = new BigDecimal("0.33");
    private static final BigDecimal LPG_EXCISE = new BigDecimal("0.18");

    private static final BigDecimal VAT_BEFORE_2024 = new BigDecimal("0.21");
    private static final BigDecimal VAT_FROM_2024 = new BigDecimal("0.23");

    private static final LocalDate VAT_CHANGE_DATE = LocalDate.of(2024, 1, 1);

    public BigDecimal calculateNetFuelPrice(Shipment shipment) {
        BigDecimal volume = shipment.getVolume();
        BigDecimal basePrice = shipment.getBasePrice();

        BigDecimal discount = BigDecimal.ZERO;

        if (volume.compareTo(DISCOUNT_LIMIT_2) > 0) {
            discount = DISCOUNT_RATE_2;
        } else if (volume.compareTo(DISCOUNT_LIMIT_1) > 0) {
            discount = DISCOUNT_RATE_1;
        }

        BigDecimal multiplier = BigDecimal.ONE.subtract(discount);

        return volume
                .multiply(basePrice)
                .multiply(multiplier)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateLogisticsCost(Shipment shipment) {
        BigDecimal distance = shipment.getDistance();
        BigDecimal volume = shipment.getVolume();

        BigDecimal logisticsCost = distance.multiply(DELIVERY_RATE);

        if (volume.compareTo(SMALL_LOAD_LIMIT) < 0) {
            logisticsCost = logisticsCost.add(SMALL_LOAD_FEE);
        }

        return logisticsCost.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateExcise(Shipment shipment) {
        BigDecimal volume = shipment.getVolume();
        BigDecimal exciseRate;

        switch (shipment.getType()) {
            case GASOLINE:
                exciseRate = GASOLINE_EXCISE;
                break;
            case DIESEL:
                exciseRate = DIESEL_EXCISE;
                break;
            case LPG:
                exciseRate = LPG_EXCISE;
                break;
            default:
                throw new IllegalStateException("Unknown fuel type");
        }

        return volume
                .multiply(exciseRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateVat(
            Shipment shipment,
            BigDecimal netFuelPrice,
            BigDecimal logisticsCost,
            BigDecimal excise
    ) {
        if (shipment.getClient() == ClientType.EXEMPT) {
            return BigDecimal.ZERO.setScale(2);
        }

        BigDecimal vatRate;

        if (shipment.getDate().isBefore(VAT_CHANGE_DATE)) {
            vatRate = VAT_BEFORE_2024;
        } else {
            vatRate = VAT_FROM_2024;
        }

        BigDecimal vatBase = netFuelPrice
                .add(logisticsCost)
                .add(excise);

        return vatBase
                .multiply(vatRate)
                .setScale(2, RoundingMode.HALF_UP);
    }


    public PricingResult calculate(Shipment shipment) {
        BigDecimal netFuelPrice = calculateNetFuelPrice(shipment);

        BigDecimal logisticsCost = calculateLogisticsCost(shipment);

        BigDecimal excise = calculateExcise(shipment);

        BigDecimal vat = calculateVat(
                shipment,
                netFuelPrice,
                logisticsCost,
                excise
        );

        BigDecimal totalTax = excise
                .add(vat)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal grandTotal = netFuelPrice
                .add(logisticsCost)
                .add(totalTax)
                .setScale(2, RoundingMode.HALF_UP);

        return new PricingResult(
                shipment.getId(),
                netFuelPrice,
                logisticsCost,
                totalTax,
                grandTotal
        );
    }
    public BigDecimal calculateExcise(
            Shipment shipment,
            BigDecimal customGasolineExcise
    ) {
        if (shipment.getType() == FuelType.GASOLINE) {
            return shipment.getVolume()
                    .multiply(customGasolineExcise)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return calculateExcise(shipment);
    }

    public BigDecimal calculateVat(
            Shipment shipment,
            BigDecimal netFuelPrice,
            BigDecimal logisticsCost,
            BigDecimal excise,
            BigDecimal customVatPercent
    ) {
        if (shipment.getClient() == ClientType.EXEMPT) {
            return BigDecimal.ZERO.setScale(2);
        }

        BigDecimal vatRate = customVatPercent.movePointLeft(2);

        BigDecimal vatBase = netFuelPrice
                .add(logisticsCost)
                .add(excise);

        return vatBase
                .multiply(vatRate)
                .setScale(2, RoundingMode.HALF_UP);
    }


    public PricingResult calculate(
            Shipment shipment,
            BigDecimal customVatPercent,
            BigDecimal customGasolineExcise
    ) {
        BigDecimal netFuelPrice = calculateNetFuelPrice(shipment);

        BigDecimal logisticsCost = calculateLogisticsCost(shipment);

        BigDecimal excise = calculateExcise(
                shipment,
                customGasolineExcise
        );

        BigDecimal vat = calculateVat(
                shipment,
                netFuelPrice,
                logisticsCost,
                excise,
                customVatPercent
        );

        BigDecimal totalTax = excise
                .add(vat)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal grandTotal = netFuelPrice
                .add(logisticsCost)
                .add(totalTax)
                .setScale(2, RoundingMode.HALF_UP);

        return new PricingResult(
                shipment.getId(),
                netFuelPrice,
                logisticsCost,
                totalTax,
                grandTotal
        );
    }


}
