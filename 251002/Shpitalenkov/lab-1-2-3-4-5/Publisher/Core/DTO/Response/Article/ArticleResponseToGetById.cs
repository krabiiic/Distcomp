using System.Runtime.Serialization;

namespace Core.DTO;

[Serializable]
[DataContract(Name="article")]
public class ArticleResponseToGetById
{
    public long Id { get; init; }
    public long CreatorId { get; init; }
    public string Title { get; init; }
    public string Content { get; init; }
}