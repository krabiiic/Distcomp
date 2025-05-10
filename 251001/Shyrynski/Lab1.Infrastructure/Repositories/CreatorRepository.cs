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
    public class CreatorRepository : ICreatorRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        private IMapper _mapper;
        public CreatorRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public CreatorResponseTo? Get(ulong id)
        {
            return _mapper.Map<CreatorResponseTo>(_database.Creators.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public CreatorResponseTo Create(Creator creator)
        {
            var creatorEntity = _mapper.Map<CreatorEntity>(creator);
            creatorEntity.Id = _currId++;
            _database.Creators.Add(creatorEntity);
            _database.SaveChanges();
            return _mapper.Map<CreatorResponseTo>(creatorEntity);
        }
        public List<CreatorResponseTo> GetAll()
        {
            var creators = _database.Creators.AsNoTracking().ToList();
            return _mapper.Map<List<CreatorResponseTo>>(creators);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.Creators.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.Creators.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public CreatorResponseTo? Update(Creator creator, ulong id)
        {
            var entity = _database.Creators.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<Creator, CreatorEntity>(creator, entity);
            _database.Creators.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<CreatorResponseTo>(entity);
        }
    }
}
