using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Request.CreateTo;

public record CreateTagRequestTo(
    [property: JsonPropertyName("name")]
    [StringLength(32, MinimumLength = 2)]
    string Name
);