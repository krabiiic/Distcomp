using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class UserService : IUserService
    {
        private IUserRepository _repository;
        private IValidator<User> _validator;
        public UserService(IUserRepository repository, IValidator<User> validator) => (_repository, _validator) = (repository, validator);
        public UserResponseTo CreateUser(User user)
        {
            var res = _validator.Validate(user);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Create(user);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public bool DeleteUser(ulong id)
        {
            try
            {
                return _repository.Delete(id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public UserResponseTo? GetUser(ulong id)
        {
            try
            {
                return _repository.Get(id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public List<UserResponseTo> GetAllUsers()
        {
            try
            {
                return _repository.GetAll();
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public UserResponseTo? UpdateUser(User user, ulong id)
        {
            var res = _validator.Validate(user);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Update(user, id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
    }
}
