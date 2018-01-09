package com.aimacademyla.model.response;

/**
 * Created by davidkim on 7/7/17.
 */
public abstract class GenericResponse {

    private String statusMessage;
    private String errorMessage;
    private int errorCode;

    protected GenericResponse(String statusMessage, String errorMessage, int errorCode) {
        this.statusMessage = statusMessage;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
