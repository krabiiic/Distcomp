using discussion.DTO.Response;

namespace discussion.DTO.Message
{
    public record OutTopicMessage(
    OperationType OperationType,
    List<NoteResponseTo> Result,
    string? ErrorMessage = null)
    {
        public bool IsSuccess => ErrorMessage == null;
    }
}
