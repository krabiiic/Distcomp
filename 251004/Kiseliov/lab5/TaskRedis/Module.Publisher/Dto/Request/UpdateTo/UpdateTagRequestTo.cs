using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;
namespace Publisher.Dto.Request.UpdateTo;

public record UpdateTagRequestTo(
    [property: JsonPropertyName("id")] long Id,
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);