using AutoMapper;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Lab1.Core.Models;
using Lab1.Infrastructure.Contexts;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Repositories
{
    public class IssueRepository : IIssueRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        private IMapper _mapper;
        public IssueRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public IssueResponseTo? Get(ulong id)
        {
            return _mapper.Map<IssueResponseTo>(_database.Issues.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public IssueResponseTo Create(Issue msg)
        {
            var messageEntity = _mapper.Map<IssueEntity>(msg);
            messageEntity.Id = _currId;
            _database.Issues.Add(messageEntity);
            _database.SaveChanges();
            return _mapper.Map<IssueResponseTo>(messageEntity);
        }
        public List<IssueResponseTo> GetAll()
        {
            var msgs = _database.Issues.AsNoTracking().ToList();
            return _mapper.Map<List<IssueResponseTo>>(msgs);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.Issues.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.Issues.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public IssueResponseTo? Update(Issue msg, ulong id)
        {
            var entity = _database.Issues.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<Issue, IssueEntity>(msg, entity);
            _database.Issues.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<IssueResponseTo>(entity);
        }
    }
}
