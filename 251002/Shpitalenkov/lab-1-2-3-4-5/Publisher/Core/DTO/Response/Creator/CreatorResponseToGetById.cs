using System.Runtime.Serialization;

namespace Core.DTO;

[Serializable]
[DataContract(Name="creator")]
public class CreatorResponseToGetById
{
    public long Id { get; init; }
    public string Login { get; init; }
    public string Firstname { get; init; }
    public string? Lastname { get; init; }
}