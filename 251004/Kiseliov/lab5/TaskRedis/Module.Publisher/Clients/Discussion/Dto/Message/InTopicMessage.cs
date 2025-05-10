using Publisher.Clients.Discussion.Dto.Request;
namespace Publisher.Clients.Discussion.Dto.Message;

public record InTopicMessage(
    OperationType OperationType,
    DiscussionNoticeRequestTo Message);