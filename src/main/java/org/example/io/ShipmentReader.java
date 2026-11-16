package org.example.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import java.io.IOException;
import java.nio.file.Path;

public class ShipmentReader {

    private final ObjectMapper mapper;

    public ShipmentReader() {
        mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
    }

    public JsonNode readShipments() throws IOException {
        return mapper.readTree(Path.of("shipments.json").toFile());
    }
}

