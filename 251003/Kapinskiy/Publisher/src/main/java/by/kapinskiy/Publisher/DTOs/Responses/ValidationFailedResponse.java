package by.kapinskiy.Publisher.DTOs.Responses;

import lombok.Getter;

import java.util.Date;
import java.util.Map;

@Getter
public class ValidationFailedResponse {
    private int errorCode;
    private Map<String, String> errors;
    private Date timestamp;

    public ValidationFailedResponse(int errorCode, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.errors = errors;
        this.timestamp = new Date();

    }
}
