package by.ryabchikov.tweet_service.exception;

public class MarkNotFoundException extends RuntimeException {
    private MarkNotFoundException(String message) {
        super(message);
    }

    public static MarkNotFoundException byId(Long id) {
        return new MarkNotFoundException(String.format("Mark with id %d not found", id));
    }
}
