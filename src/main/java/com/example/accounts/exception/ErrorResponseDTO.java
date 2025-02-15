package com.example.accounts.exception;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ErrorResponseDTO {


    private String message;
    private int code;
    private int status;
    private String developerMessage;
    private String path;
    private String timestamp;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    private List<String> errorList;


    public List<String> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<String> errorList) {
        this.errorList = errorList;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorResponseDTO(final String message) {
        super();
        this.message = message;
        timestamp = formatter.format(LocalDateTime.now());
    }

    public ErrorResponseDTO(final String message, final String developerMessage, int code, int status, String path) {
        super();
        this.message = message;
        this.developerMessage = developerMessage;
        this.code = code;
        this.status = status;
        this.path = path;
        timestamp = formatter.format(LocalDateTime.now());
    }
}
