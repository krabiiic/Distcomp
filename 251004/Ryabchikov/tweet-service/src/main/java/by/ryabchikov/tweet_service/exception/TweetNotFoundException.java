package by.ryabchikov.tweet_service.exception;

public class TweetNotFoundException extends RuntimeException {
    private TweetNotFoundException(String message) {
        super(message);
    }

    public static TweetNotFoundException byId(Long id) {
        return new TweetNotFoundException(String.format("Tweet with id %d not found", id));
    }
}
