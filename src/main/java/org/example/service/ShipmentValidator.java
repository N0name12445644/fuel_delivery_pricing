package org.example.service;

import org.example.model.FuelType;
import org.example.model.ClientType;
import java.util.Locale;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.model.ErrorCode;
import org.example.model.ValidationError;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.math.BigDecimal;
import org.example.model.ValidationResult;
import org.example.model.Shipment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShipmentValidator {

    public ValidationError checkMissingFields(JsonNode node) {
        String[] requiredFields = {
                "id",
                "type",
                "client",
                "date",
                "volume",
                "distance",
                "basePrice"
        };

        for (String field : requiredFields) {
            if (!node.hasNonNull(field)) {
                JsonNode idNode = node.get("id");

                String id = idNode != null && !idNode.isNull()
                        ? idNode.asText()
                        : "";

                return new ValidationError(id, ErrorCode.MISSING_FIELD);
            }
        }

        return null;
    }

    public ValidationError checkFuelType(JsonNode node) {
        JsonNode typeNode = node.get("type");

        if (typeNode == null || typeNode.isNull()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.MISSING_FIELD
            );
        }

        if (!typeNode.isTextual()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.UNKNOWN_TYPE
            );
        }

        String type = typeNode.asText().trim().toUpperCase(java.util.Locale.ROOT);

        try {
            FuelType.valueOf(type);
            return null;
        } catch (IllegalArgumentException e) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.UNKNOWN_TYPE
            );
        }
    }


    public ValidationError checkClientType(JsonNode node) {
        JsonNode clientNode = node.get("client");

        if (clientNode == null || clientNode.isNull()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.MISSING_FIELD
            );
        }

        if (!clientNode.isTextual()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.UNKNOWN_CLIENT
            );
        }

        String client = clientNode.asText()
                .trim()
                .toUpperCase(Locale.ROOT);

        try {
            ClientType.valueOf(client);
            return null;
        } catch (IllegalArgumentException e) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.UNKNOWN_CLIENT
            );
        }
    }

    public ValidationError checkDate(JsonNode node) {
        JsonNode dateNode = node.get("date");

        if (dateNode == null || dateNode.isNull()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.MISSING_FIELD
            );
        }

        if (!dateNode.isTextual()) {
            return new ValidationError(
                    node.path("id").asText(""),
                    ErrorCode.INVALID_DATE
            );
        }

        String date = dateNode.asText();

        DateTimeFormatter isoFormatter = DateTimeFormatter
                .ofPattern("uuuu-MM-dd")
                .withResolverStyle(ResolverStyle.STRICT);

        DateTimeFormatter europeanFormatter = DateTimeFormatter
                .ofPattern("dd/MM/uuuu")
                .withResolverStyle(ResolverStyle.STRICT);

        try {
            LocalDate.parse(date, isoFormatter);
            return null;
        } catch (DateTimeParseException e) {
            try {
                LocalDate.parse(date, europeanFormatter);
                return null;
            } catch (DateTimeParseException ex) {
                return new ValidationError(
                        node.path("id").asText(""),
                        ErrorCode.INVALID_DATE
                );
            }
        }
    }


    public ValidationError checkNumericValues(JsonNode node) {
        JsonNode volumeNode = node.get("volume");
        JsonNode distanceNode = node.get("distance");
        JsonNode priceNode = node.get("basePrice");

        String id = node.path("id").asText("");

        if (!volumeNode.isNumber()
                || !distanceNode.isNumber()
                || !priceNode.isNumber()) {
            return new ValidationError(id, ErrorCode.INVALID_VALUE);
        }

        BigDecimal volume = volumeNode.decimalValue();
        BigDecimal distance = distanceNode.decimalValue();
        BigDecimal basePrice = priceNode.decimalValue();

        if (volume.compareTo(BigDecimal.ZERO) <= 0
                || distance.compareTo(BigDecimal.ZERO) < 0
                || basePrice.compareTo(BigDecimal.ZERO) < 0) {
            return new ValidationError(id, ErrorCode.INVALID_VALUE);
        }

        return null;
    }


    public ValidationResult validateAll(JsonNode shipments) {
        List<Shipment> validShipments = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();
        Set<String> seenIds = new HashSet<>();

        for (JsonNode node : shipments) {
            JsonNode idNode = node.get("id");

            String id = idNode == null || idNode.isNull()
                    ? ""
                    : idNode.asText();

            boolean duplicate = idNode != null
                    && !idNode.isNull()
                    && !seenIds.add(id);

            ValidationError error;

            if (duplicate) {
                error = new ValidationError(id, ErrorCode.DUPLICATE_ID);
            } else {
                error = checkMissingFields(node);

                if (error == null) {
                    error = checkFuelType(node);
                }

                if (error == null) {
                    error = checkClientType(node);
                }

                if (error == null) {
                    error = checkDate(node);
                }

                if (error == null) {
                    error = checkNumericValues(node);
                }
            }

            if (error != null) {
                errors.add(error);
            } else {
                Shipment validShipment = createShipment(node);
                validShipments.add(validShipment);
            }
        }

        return new ValidationResult(validShipments, errors);
    }


    private Shipment createShipment(JsonNode node) {
        String id = node.get("id").asText();

        FuelType type = FuelType.valueOf(
                node.get("type").asText().trim().toUpperCase(Locale.ROOT)
        );

        ClientType client = ClientType.valueOf(
                node.get("client").asText().trim().toUpperCase(Locale.ROOT)
        );

        String dateText = node.get("date").asText();
        LocalDate date;

        if (dateText.contains("/")) {
            date = LocalDate.parse(
                    dateText,
                    DateTimeFormatter.ofPattern("dd/MM/uuuu")
            );
        } else {
            date = LocalDate.parse(
                    dateText,
                    DateTimeFormatter.ofPattern("uuuu-MM-dd")
            );
        }

        BigDecimal volume = node.get("volume").decimalValue();
        BigDecimal distance = node.get("distance").decimalValue();
        BigDecimal basePrice = node.get("basePrice").decimalValue();

        return new Shipment(
                id,
                type,
                client,
                date,
                volume,
                distance,
                basePrice
        );
    }


}
