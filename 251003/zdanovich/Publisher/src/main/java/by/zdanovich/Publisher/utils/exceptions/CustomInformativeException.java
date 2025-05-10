package by.zdanovich.Publisher.utils.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomInformativeException extends RuntimeException {
    private int errorCode;
    private HttpStatus httpStatus;

    public CustomInformativeException(String message, int errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

}
