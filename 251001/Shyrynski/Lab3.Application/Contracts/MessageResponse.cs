namespace Lab3.Application.Contracts
{
    public record MessageResponse(string Id, int StatusCode, string? Data = null);
}
