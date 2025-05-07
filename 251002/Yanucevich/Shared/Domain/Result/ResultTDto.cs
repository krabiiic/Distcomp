using System.Text.Json.Serialization;
using Shared.Domain;

public class ResultDto<TValue> : ResultDto
{
    [JsonPropertyName("value")]
    public TValue? Value { get; set; }
}
