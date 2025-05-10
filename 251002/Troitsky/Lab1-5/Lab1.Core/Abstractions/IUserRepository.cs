using Lab1.Core.Contracts;
using Lab1.Core.Models;
using System.Numerics;
namespace Lab1.Core.Abstractions
{
    public interface IUserRepository
    {
        // возвращает DTO
        UserResponseTo? Get(ulong id);
        UserResponseTo Create(User user); 
        List<UserResponseTo> GetAll();
        bool Delete(ulong id);
        UserResponseTo? Update(User user, ulong id);
    }
}
