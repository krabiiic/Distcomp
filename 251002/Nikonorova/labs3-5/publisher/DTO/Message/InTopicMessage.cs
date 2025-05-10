using publisher.ToDiscussion.DTO;

namespace publisher.DTO.Message
{
    public record InTopicMessage(
    OperationType OperationType,
    DiscussionNoteRequestTo Message);

}
