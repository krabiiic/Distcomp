using AutoMapper;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Lab1.Core.Models;
using Lab1.Infrastructure.Contexts;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Repositories
{
    public class NewsRepository : INewsRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        private IMapper _mapper;
        public NewsRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public NewsResponseTo? Get(ulong id)
        {
            return _mapper.Map<NewsResponseTo>(_database.News.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public NewsResponseTo Create(News msg)
        {
            var newsEntity = _mapper.Map<NewsEntity>(msg);
            newsEntity.Id = _currId;
            _database.News.Add(newsEntity);
            _database.SaveChanges();
            return _mapper.Map<NewsResponseTo>(newsEntity);
        }
        public List<NewsResponseTo> GetAll()
        {
            var msgs = _database.News.AsNoTracking().ToList();
            return _mapper.Map<List<NewsResponseTo>>(msgs);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.News.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.News.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public NewsResponseTo? Update(News msg, ulong id)
        {
            var entity = _database.News.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<News, NewsEntity>(msg, entity);
            _database.News.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<NewsResponseTo>(entity);
        }
    }
}
