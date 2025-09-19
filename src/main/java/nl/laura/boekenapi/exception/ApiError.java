package nl.laura.boekenapi.exception;

import java.time.OffsetDateTime;

public class ApiError {

    private OffsetDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError(String message) {
        this.timestamp = OffsetDateTime.now();
        this.status = 500;
        this.error = "Internal Server Error";
        this.message = message;
    }

    public ApiError(OffsetDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}
