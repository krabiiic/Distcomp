
namespace Lab1.Application.Contracts
{
    // PostRequestTo сериализуется в Data поля общего PostRequest
    public record PostRequestTo(ulong NewsId, string Content);


}
