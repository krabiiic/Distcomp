using System.Runtime.Serialization;
using System.Text.Json.Serialization;

namespace Core.DTO;

[Serializable]
[DataContract(Name="tag")]
public class TagResponseToGetById
{
    public long Id { get; init; }
    public string Name { get; init; }
}