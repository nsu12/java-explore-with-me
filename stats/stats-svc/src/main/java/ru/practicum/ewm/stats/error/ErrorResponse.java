package ru.practicum.ewm.status.error;

class ErrorResponse {
    public final String error;

    public ErrorResponse(String message) {
        this.error = message;
    }

    public String getError() {
        return error;
    }
}

