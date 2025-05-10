using AutoMapper;
using Lab1.Core.Abstractions;
using Lab1.Core.Contracts;
using Lab1.Core.Models;
using Lab1.Infrastructure.Contexts;
using Lab1.Infrastructure.Entities;
using Microsoft.EntityFrameworkCore;
namespace Lab1.Infrastructure.Repositories
{
    public class StickerRepository : IStickerRepository
    {
        private DataContext _database;
        private static ulong _currId = 1;
        private IMapper _mapper;
        public StickerRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
        public StickerResponseTo? Get(ulong id)
        {
            return _mapper.Map<StickerResponseTo>(_database.Stickers.AsNoTracking().FirstOrDefault(m => m.Id == id));
        }
        public StickerResponseTo Create(Sticker msg)
        {
            var messageEntity = _mapper.Map<StickerEntity>(msg);
            messageEntity.Id = _currId;
            _database.Stickers.Add(messageEntity);
            _database.SaveChanges();
            return _mapper.Map<StickerResponseTo>(messageEntity);
        }
        public List<StickerResponseTo> GetAll()
        {
            var msgs = _database.Stickers.AsNoTracking().ToList();
            return _mapper.Map<List<StickerResponseTo>>(msgs);
        }
        public bool Delete(ulong id)
        {
            var entity = _database.Stickers.FirstOrDefault(m => m.Id == id);
            if (entity == null) return false;
            _database.Stickers.Remove(entity);
            _database.SaveChanges();
            return true;
        }
        public StickerResponseTo? Update(Sticker msg, ulong id)
        {
            var entity = _database.Stickers.FirstOrDefault(m => m.Id == id);
            if (entity == null) return null;
            _mapper.Map<Sticker, StickerEntity>(msg, entity);
            _database.Stickers.Update(entity);
            _database.SaveChanges();
            return _mapper.Map<StickerResponseTo>(entity);
        }
    }
}
