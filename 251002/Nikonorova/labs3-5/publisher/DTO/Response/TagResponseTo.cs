using System.Text.Json.Serialization;

namespace publisher.DTO.Response
{
    public record TagResponseTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("name")] string Name
);
}
