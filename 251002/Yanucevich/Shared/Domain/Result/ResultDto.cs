using System.Text.Json.Serialization;
using Shared.Domain;

public class ResultDto
{
    [JsonPropertyName("is_success")]
    public bool IsSuccess { get; set; }

    [JsonPropertyName("error")]
    public ErrorDto Error { get; set; }
}
