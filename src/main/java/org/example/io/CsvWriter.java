
package org.example.io;

import org.example.model.PricingResult;
import org.example.model.ValidationError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvWriter {

    public void writePricingResults(
            String filename,
            List<PricingResult> results
    ) throws IOException {
        Path outputDirectory = Path.of("output");
        Files.createDirectories(outputDirectory);

        Path outputFile = outputDirectory.resolve(filename);

        StringBuilder csv = new StringBuilder();

        csv.append("Artūr_Kuc\n");
        csv.append("ShipmentID,NetFuelPrice,LogisticsCost,TotalTax,GrandTotal\n");

        for (PricingResult result : results) {
            csv.append(result.getShipmentId()).append(",");
            csv.append(result.getNetFuelPrice().toPlainString()).append(",");
            csv.append(result.getLogisticsCost().toPlainString()).append(",");
            csv.append(result.getTotalTax().toPlainString()).append(",");
            csv.append(result.getGrandTotal().toPlainString()).append("\n");
        }

        Files.writeString(outputFile, csv.toString());
    }

    public void writeErrors(List<ValidationError> errors) throws IOException {
        Path outputDirectory = Path.of("output");
        Files.createDirectories(outputDirectory);

        Path outputFile = outputDirectory.resolve("errors.csv");

        StringBuilder csv = new StringBuilder();

        csv.append("Artūr_Kuc\n");
        csv.append("ShipmentID,ErrorCode\n");

        for (ValidationError error : errors) {
            csv.append(error.getShipmentId()).append(",");
            csv.append(error.getErrorCode()).append("\n");
        }

        Files.writeString(outputFile, csv.toString());
    }
}
