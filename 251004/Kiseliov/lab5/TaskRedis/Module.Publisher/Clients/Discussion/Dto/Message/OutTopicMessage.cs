using Publisher.Clients.Discussion.Dto.Response;
namespace Publisher.Clients.Discussion.Dto.Message;

public record OutTopicMessage(
    OperationType OperationType,
    List<DiscussionNoticeResponseTo> Result,
    string? ErrorMessage = null)
{
    public bool IsSuccess => ErrorMessage == null;
}
