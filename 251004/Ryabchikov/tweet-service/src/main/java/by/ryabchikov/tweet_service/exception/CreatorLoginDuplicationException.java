package by.ryabchikov.tweet_service.exception;

public class CreatorLoginDuplicationException extends RuntimeException {
    private CreatorLoginDuplicationException(String message) {
        super(message);
    }

    public static CreatorLoginDuplicationException byLogin(String login) {
        return new CreatorLoginDuplicationException(String.format("Creator with login %s already exists.", login));
    }
}
