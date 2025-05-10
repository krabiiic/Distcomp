using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace lab2_jpa.DTO.Request.CreateTo
{
    public record CreateTagRequestTo(
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);
}
