using System.Runtime.Serialization;

namespace Core.DTO;

[Serializable]
[DataContract(Name="message")]
public class MessageResponseToGetById
{
    public long Id { get; init; }
    public long ArticleId { get; init; }
    public string Content { get; init; }
}