using AutoMapper;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Lab1.Core.Models;
using Lab1.Infrastructure.Contexts;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
using System.Text.Json;
namespace Lab1.Infrastructure.Repositories
{
    public class UserRepository : IUserRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        // для преобразований: Модель домена -> сущность БД, Сущность БД -> DTO
        private IMapper _mapper;
        public UserRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public UserResponseTo? Get(ulong id)
        {
            return _mapper.Map<UserResponseTo>(_database.Users.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public UserResponseTo Create(User user)
        {
            var userEntity = _mapper.Map<UserEntity>(user);
            userEntity.Id = _currId++;
            _database.Users.Add(userEntity);
            _database.SaveChanges();
            return _mapper.Map<UserResponseTo>(userEntity);
        }
        public List<UserResponseTo> GetAll()
        {
            var users = _database.Users.AsNoTracking().ToList();
            return _mapper.Map<List<UserResponseTo>>(users);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.Users.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.Users.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public UserResponseTo? Update(User user, ulong id)
        {
            var entity = _database.Users.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<User, UserEntity>(user, entity);
            _database.Users.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<UserResponseTo>(entity);
        }
    }
}
