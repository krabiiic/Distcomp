namespace Lab1.Application.Contracts
{
    // Обобщённый “конверт” для сообщений
    // используется при публикации события в шину
    public record PostRequest(string Id, string Action, string? Data = null);

}