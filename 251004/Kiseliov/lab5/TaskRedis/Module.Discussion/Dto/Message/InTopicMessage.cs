using Discussion.Dto.Request;
namespace Discussion.Dto.Message;

public record InTopicMessage(
    OperationType OperationType,
    NoticeRequestTo Message);