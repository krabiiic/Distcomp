using System.Text.Json.Serialization;

namespace TaskSQL.Dto.Response;

public record CreatorResponseTo(
    [property: JsonPropertyName("id")] long id,
    [property: JsonPropertyName("login")] string Login,
    [property: JsonPropertyName("password")]
    string Password,
    [property: JsonPropertyName("firstname")]
    string FirstName,
    [property: JsonPropertyName("lastname")]
    string LastName
);