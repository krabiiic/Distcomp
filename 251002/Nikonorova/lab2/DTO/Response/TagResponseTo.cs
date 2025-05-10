using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Response
{
    public record TagResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("name")] string Name
);
}
