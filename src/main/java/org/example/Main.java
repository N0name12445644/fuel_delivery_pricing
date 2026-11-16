
package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.io.ConsoleInput;
import org.example.io.CsvWriter;
import org.example.io.ShipmentReader;
import org.example.model.PricingResult;
import org.example.model.Shipment;
import org.example.model.ValidationResult;
import org.example.service.PricingService;
import org.example.service.ShipmentValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        ShipmentReader reader = new ShipmentReader();
        ShipmentValidator validator = new ShipmentValidator();
        PricingService pricingService = new PricingService();
        CsvWriter csvWriter = new CsvWriter();
        ConsoleInput consoleInput = new ConsoleInput();

        JsonNode shipments = reader.readShipments();
        ValidationResult validationResult = validator.validateAll(shipments);

        List<PricingResult> task1Results = new ArrayList<>();

        for (Shipment shipment : validationResult.getValidShipments()) {
            PricingResult result = pricingService.calculate(shipment);
            task1Results.add(result);
        }

        csvWriter.writePricingResults("task1.csv", task1Results);
        csvWriter.writeErrors(validationResult.getErrors());

        BigDecimal customVatPercent = consoleInput.readNonNegativeDecimal(
                "Enter custom VAT rate for Private clients (%): "
        );

        BigDecimal customGasolineExcise = consoleInput.readNonNegativeDecimal(
                "Enter custom Excise rate for Gasoline: "
        );

        List<PricingResult> task2Results = new ArrayList<>();

        for (Shipment shipment : validationResult.getValidShipments()) {
            PricingResult result = pricingService.calculate(
                    shipment,
                    customVatPercent,
                    customGasolineExcise
            );

            task2Results.add(result);
        }

        csvWriter.writePricingResults("task2.csv", task2Results);

        System.out.println("Valid shipments: " + task1Results.size());
        System.out.println("Errors: " + validationResult.getErrors().size());
        System.out.println("Created output/task1.csv");
        System.out.println("Created output/task2.csv");
        System.out.println("Created output/errors.csv");
    }
}
