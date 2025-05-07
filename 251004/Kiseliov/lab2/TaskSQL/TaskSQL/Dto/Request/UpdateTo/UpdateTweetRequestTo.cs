using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Request.UpdateTo;

public record UpdateTweetRequestTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("creatorId")]
    long creator_id,
    [property: JsonPropertyName("title")]
    [StringLength(64, MinimumLength = 2)]
    string Title,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 4)]
    string Content
);