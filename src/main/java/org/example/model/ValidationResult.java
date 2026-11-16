
package org.example.model;

import java.util.List;

public class ValidationResult {

    private final List<Shipment> validShipments;
    private final List<ValidationError> errors;

    public ValidationResult(
            List<Shipment> validShipments,
            List<ValidationError> errors
    ) {
        this.validShipments = List.copyOf(validShipments);
        this.errors = List.copyOf(errors);
    }

    public List<Shipment> getValidShipments() {
        return validShipments;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
