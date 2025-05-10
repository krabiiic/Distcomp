package publisher.exceptions;

public class KafkaException extends RuntimeException {
    public KafkaException(String exceptionSimpleName, String message) {
        super(message);
        simpleName = exceptionSimpleName;
    }

    private String simpleName;
}
