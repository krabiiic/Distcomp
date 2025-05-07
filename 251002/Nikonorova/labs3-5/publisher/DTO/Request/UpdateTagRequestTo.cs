using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace publisher.DTO.Request
{
    public record UpdateTagRequestTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);
}
