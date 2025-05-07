using AutoMapper;
using Lab3.Core.Abstractions;
using Lab3.Core.Contracts;
using Lab3.Core.Models;
using Lab3.Infrastructure.Entities;
using MongoDB.Driver;
using System.Text.Json;

namespace Lab3.Infrastructure.Repositories
{
    public class MessageRepository : IMessageRepository
    {
        private IMongoCollection<MessageEntity> _messages;
        private static ulong _currId = 1;
        private readonly IMapper _mapper;
        public MessageRepository(IMongoDatabase db, IMapper mapper)
        {
            (_messages, _mapper) = (db.GetCollection<MessageEntity>("distcomp"), mapper);
        }
        public MessageResponseTo? Get(ulong id)
        {
            //if (_db.KeyExists(id.ToString()))
            //    return _mapper.Map<MessageResponseTo>(JsonSerializer.Deserialize<MessageEntity>(_db.StringGet(id.ToString())));
            return _mapper.Map<MessageResponseTo>(_messages.Find(m => m.Id == id).FirstOrDefault());
        }
        public MessageResponseTo Create(Message msg)
        {
            var messageEntity = _mapper.Map<MessageEntity>(msg);
            messageEntity.Id = _currId++;
            _messages.InsertOne(messageEntity);
            return _mapper.Map<MessageResponseTo>(messageEntity);
        }
        public List<MessageResponseTo> GetAll()
        {
            var msgs = _messages.Find(_ => true).ToList();
            return _mapper.Map<List<MessageResponseTo>>(msgs);
        }
        public bool Delete(ulong id)
        {
            var entity = _messages.Find(m => m.Id == id).FirstOrDefault();
            if (entity == null) return false;
            _messages.DeleteOne(e => e.Id == id);
            return true;
        }
        public MessageResponseTo? Update(Message msg, ulong id)
        {
            var entity = _messages.Find(m => m.Id == id).FirstOrDefault();
            if (entity == null) return null;
            _mapper.Map(msg, entity);
            _messages.ReplaceOne(m => m.Id == id, entity);
            return _mapper.Map<MessageResponseTo>(entity);
        }
    }
}
