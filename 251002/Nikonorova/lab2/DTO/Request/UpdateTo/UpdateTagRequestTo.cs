using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.UpdateTo
{
    public record UpdateTagRequestTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);
}
