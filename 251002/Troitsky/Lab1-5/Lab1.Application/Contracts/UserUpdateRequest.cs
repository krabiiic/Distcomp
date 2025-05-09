
namespace Lab1.Application.Contracts
{
    public record UserUpdateRequest(ulong Id, string Login, string Password, string FirstName, string LastName);
}
