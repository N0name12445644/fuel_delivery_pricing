
package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Shipment {

    private final String id;
    private final FuelType type;
    private final ClientType client;
    private final LocalDate date;
    private final BigDecimal volume;
    private final BigDecimal distance;
    private final BigDecimal basePrice;

    public Shipment(
            String id,
            FuelType type,
            ClientType client,
            LocalDate date,
            BigDecimal volume,
            BigDecimal distance,
            BigDecimal basePrice
    ) {
        this.id = id;
        this.type = type;
        this.client = client;
        this.date = date;
        this.volume = volume;
        this.distance = distance;
        this.basePrice = basePrice;
    }

    public String getId() {
        return id;
    }

    public FuelType getType() {
        return type;
    }

    public ClientType getClient() {
        return client;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }
}
