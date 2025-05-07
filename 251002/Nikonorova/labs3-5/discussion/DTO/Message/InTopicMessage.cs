using discussion.DTO.Request;
namespace discussion.DTO.Message
{
    public record InTopicMessage(
    OperationType OperationType,
    NoteRequestTo Message);

}
