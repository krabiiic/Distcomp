using System;
using System.Text.Json.Serialization;

namespace Shared.Domain;

public class ErrorDto
{
    [JsonPropertyName("http_status_code")]
    public int HttpStatusCode { get; set; }

    [JsonPropertyName("error_target")]
    public int ErrorTarget { get; set; }

    [JsonPropertyName("error_class")]
    public int ErrorClass { get; set; }

    [JsonPropertyName("error_message")]
    public string ErrorMessage { get; set; }
}
