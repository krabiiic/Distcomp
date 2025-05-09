namespace Lab3.Application.Contracts
{
    public record PostResponse(string Id, int StatusCode, string? Data = null);
}
