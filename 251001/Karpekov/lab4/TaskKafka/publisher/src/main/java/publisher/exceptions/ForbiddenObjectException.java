package publisher.exceptions;

public class ForbiddenObjectException extends RuntimeException {
    public ForbiddenObjectException(String msg) {
        super(msg);
    }

    public ForbiddenObjectException(Throwable cause) {
        super(cause);
    }
}
