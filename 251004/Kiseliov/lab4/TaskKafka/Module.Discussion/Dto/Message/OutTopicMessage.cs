using Discussion.Dto.Response;
namespace Discussion.Dto.Message;

public record OutTopicMessage(
    OperationType OperationType,
    List<NoticeResponseTo> Result,
    string? ErrorMessage = null)
{
    public bool IsSuccess => ErrorMessage == null;
}
