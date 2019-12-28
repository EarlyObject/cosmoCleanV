package com.space.model.response;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field. Please check documentation for required fields"),
    RECORD_ALREADY_EXISTS("Record already exists"),
    NO_RECORD_FOUND("Record with provided id is not found"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    WRONG_DATE("Wrong date"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    WRONG_ARGUMENT("Wrong argument");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

