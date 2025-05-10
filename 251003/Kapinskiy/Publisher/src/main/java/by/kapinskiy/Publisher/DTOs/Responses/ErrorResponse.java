package by.kapinskiy.Publisher.DTOs.Responses;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorResponse {
    private int errorCode;
    private String errorMessage;
    private Date timestamp;

    public ErrorResponse(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.timestamp = new Date();
    }
}
