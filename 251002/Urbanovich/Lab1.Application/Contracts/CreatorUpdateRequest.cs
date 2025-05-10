
namespace Lab1.Application.Contracts
{
    public record CreatorUpdateRequest(ulong Id, string Login, string Password, string FirstName, string LastName);
}
