using publisher.DTO.Response;

namespace publisher.DTO.Message
{
    public record OutTopicMessage(
    OperationType OperationType,
    List<NoteResponseTo> Result,
    string? ErrorMessage = null)
    {
        public bool IsSuccess => ErrorMessage == null;
    }
}
