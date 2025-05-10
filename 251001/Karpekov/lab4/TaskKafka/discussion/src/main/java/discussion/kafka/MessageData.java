package discussion.kafka;


import discussion.modelDTOs.PostRequestTo;
import discussion.modelDTOs.PostResponseTo;

import java.util.List;

public record MessageData(
        Operation operation,
        Long itemId,
        PostRequestTo requestTO,
        List<PostResponseTo> responseTOs,
        ExceptionData exception
) {
    public MessageData(Operation operation) {
        this(operation, null, null, null, null);
    }

    public MessageData(Operation operation, Long itemId) {
        this(operation, itemId, null, null, null);
    }

    public MessageData(Operation operation, PostRequestTo requestTO) {
        this(operation, null, requestTO, null, null);
    }

    public MessageData(Operation operation, List<PostResponseTo> responseTOs) {
        this(operation, null, null, responseTOs, null);
    }

    public MessageData(ExceptionData exception) {
        this(Operation.EXCEPTION, null, null, null, exception);
    }

    public enum Operation {
        GET_ALL,
        GET_BY_ID,
        CREATE,
        UPDATE,
        DELETE_BY_ID,
        EXCEPTION
    }

    public record ExceptionData(
            String simpleName,
            String message
    ) { }
}
