package by.ryabchikov.tweet_service.exception;

public class TweetTitleDuplicationException extends RuntimeException {
    private TweetTitleDuplicationException(String message) {
        super(message);
    }

    public static TweetTitleDuplicationException byTitle(String title) {
        return new TweetTitleDuplicationException(String.format("Tweet with title %s already exists.", title));
    }
}