using System.ComponentModel.DataAnnotations;
using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Request.CreateTo;

public record CreateNoticeRequestTo(
    [property: JsonPropertyName("tweetid")]
    long Tweetid,
    [property: JsonPropertyName("content")]
    [StringLength(2048, MinimumLength = 2)]
    string Content
);