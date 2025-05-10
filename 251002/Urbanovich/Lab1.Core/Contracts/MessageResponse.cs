namespace Lab1.Core.Contracts
{
    public record MessageResponse(string Id, int StatusCode, string? Data = null);
}
