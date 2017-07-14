package com.aimacademyla.controller;

/**
 * Created by davidkim on 7/7/17.
 */
public abstract class GenericResponse {

    protected String statusMessage;
    protected String errorMessage;
    protected int errorCode;

    public GenericResponse(String statusMessage, String errorMessage, int errorCode) {
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
