
package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.io.ShipmentReader;
import org.example.model.ValidationResult;
import org.example.service.ShipmentValidator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        ShipmentReader reader = new ShipmentReader();
        ShipmentValidator validator = new ShipmentValidator();

        JsonNode shipments = reader.readShipments();
        ValidationResult result = validator.validateAll(shipments);

        System.out.println("Valid shipments: " + result.getValidShipments().size());
        System.out.println("Errors: " + result.getErrors().size());
    }
}
