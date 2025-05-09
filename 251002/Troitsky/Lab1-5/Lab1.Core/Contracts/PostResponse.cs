namespace Lab1.Core.Contracts
{
    public record PostResponse(string Id, int StatusCode, string? Data = null);
}
