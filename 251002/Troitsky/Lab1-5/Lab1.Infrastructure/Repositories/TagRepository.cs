using AutoMapper;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Lab1.Core.Models;
using Lab1.Infrastructure.Contexts;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Repositories
{
    public class TagRepository : ITagRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        private IMapper _mapper;
        public TagRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public TagResponseTo? Get(ulong id)
        {
            return _mapper.Map<TagResponseTo>(_database.Tags.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public TagResponseTo Create(Tag msg)
        {
            var tagEntity = _mapper.Map<TagEntity>(msg);
            tagEntity.Id = _currId;
            _database.Tags.Add(tagEntity);
            _database.SaveChanges();
            return _mapper.Map<TagResponseTo>(tagEntity);
        }
        public List<TagResponseTo> GetAll()
        {
            var msgs = _database.Tags.AsNoTracking().ToList();
            return _mapper.Map<List<TagResponseTo>>(msgs);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.Tags.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.Tags.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public TagResponseTo? Update(Tag msg, ulong id)
        {
            var entity = _database.Tags.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<Tag, TagEntity>(msg, entity);
            _database.Tags.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<TagResponseTo>(entity);
        }
    }
}
