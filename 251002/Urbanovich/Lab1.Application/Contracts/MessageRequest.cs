namespace Lab1.Application.Contracts
{
    public record MessageRequest(string Id, string Action, string? Data = null);
}
