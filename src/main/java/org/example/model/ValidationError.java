package org.example.model;

public class ValidationError {

    private final String shipmentId;
    private final ErrorCode errorCode;

    public ValidationError(String shipmentId, ErrorCode errorCode) {
        this.shipmentId = shipmentId;
        this.errorCode = errorCode;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
