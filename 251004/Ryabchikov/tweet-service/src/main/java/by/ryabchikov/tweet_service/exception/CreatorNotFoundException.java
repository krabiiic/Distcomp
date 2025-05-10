package by.ryabchikov.tweet_service.exception;

public class CreatorNotFoundException extends RuntimeException {
    private CreatorNotFoundException(String message) {
        super(message);
    }

    public static CreatorNotFoundException byId(Long id) {
        return new CreatorNotFoundException(String.format("Creator with id %d not found", id));
    }
}
