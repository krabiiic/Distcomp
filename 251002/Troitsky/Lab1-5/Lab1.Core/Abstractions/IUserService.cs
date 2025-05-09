using Lab1.Core.Contracts;
using Lab1.Core.Models;
using System.Numerics;

namespace Lab1.Core.Abstractions
{
    public interface IUserService
    {
        UserResponseTo? GetUser(ulong id);
        // Создание новой сущности по модели домена - возвращает DTO созданной сущности
        UserResponseTo CreateUser(User user);
        bool DeleteUser(ulong id);
        List<UserResponseTo> GetAllUsers();
        UserResponseTo? UpdateUser (User user, ulong id);
    }
}
