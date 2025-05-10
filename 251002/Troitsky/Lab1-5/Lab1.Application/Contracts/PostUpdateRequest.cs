
namespace Lab1.Application.Contracts
{
    // PostUpdateRequest сериализуется в Data поля общего PostRequest

    public record PostUpdateRequest(ulong Id, ulong NewsId, string Content);
}
