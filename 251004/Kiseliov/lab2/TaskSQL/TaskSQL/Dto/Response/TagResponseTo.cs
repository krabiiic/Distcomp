using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Response;

public record TagResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("name")] string Name
);