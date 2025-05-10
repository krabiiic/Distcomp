package lab.exceptions;

public class IllegalFieldDataException extends RuntimeException {
    public IllegalFieldDataException(String msg) {
        super(msg);
    }

    public IllegalFieldDataException(Throwable cause) {
        super(cause);
    }
}
