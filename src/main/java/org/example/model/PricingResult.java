package org.example.model;

import java.math.BigDecimal;

public class PricingResult {

    private final String shipmentId;
    private final BigDecimal netFuelPrice;
    private final BigDecimal logisticsCost;
    private final BigDecimal totalTax;
    private final BigDecimal grandTotal;

    public PricingResult(
            String shipmentId,
            BigDecimal netFuelPrice,
            BigDecimal logisticsCost,
            BigDecimal totalTax,
            BigDecimal grandTotal
    ) {
        this.shipmentId = shipmentId;
        this.netFuelPrice = netFuelPrice;
        this.logisticsCost = logisticsCost;
        this.totalTax = totalTax;
        this.grandTotal = grandTotal;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public BigDecimal getNetFuelPrice() {
        return netFuelPrice;
    }

    public BigDecimal getLogisticsCost() {
        return logisticsCost;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public BigDecimal getGrandTotal() {
        return grandTotal;
    }
}
