
namespace Lab1.Application.Contracts
{
    public record NewsUpdateRequest(ulong Id, ulong UserId, string Title, string Content);
}
