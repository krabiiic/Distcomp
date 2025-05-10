using AutoMapper;
using Lab3.Core.Abstractions;
using Lab3.Core.Contracts;
using Lab3.Core.Models;
using Lab3.Infrastructure.Entities;
using MongoDB.Driver;

namespace Lab3.Infrastructure.Repositories
{
    public class PostRepository : IPostRepository
    {
        private IMongoCollection<PostEntity> _posts;
        private static ulong _currId = 1;
        private readonly IMapper _mapper;
        public PostRepository(IMongoDatabase db, IMapper mapper)
        {
            // подключение к коллекции distcomp в MongoDB для хранения постов
            (_posts, _mapper) = (db.GetCollection<PostEntity>("distcomp"), mapper);
        }
        public PostResponseTo? Get(ulong id)
        {
            return _mapper.Map<PostResponseTo>(_posts.Find(m => m.Id == id).FirstOrDefault());
        }
        public PostResponseTo Create(Post post)
        {
            var postEntity = _mapper.Map<PostEntity>(post);
            postEntity.Id = _currId++;
            _posts.InsertOne(postEntity);
            return _mapper.Map<PostResponseTo>(postEntity);
        }
        public List<PostResponseTo> GetAll()
        {
            var psts = _posts.Find(_ => true).ToList();
            return _mapper.Map<List<PostResponseTo>>(psts);
        }
        public bool Delete(ulong id)
        {
            var entity = _posts.Find(m => m.Id == id).FirstOrDefault();
            if (entity == null) return false;
            _posts.DeleteOne(e => e.Id == id);
            return true;
        }
        public PostResponseTo? Update(Post post, ulong id)
        {
            var entity = _posts.Find(m => m.Id == id).FirstOrDefault();
            if (entity == null) return null;
            _mapper.Map(post, entity);
            _posts.ReplaceOne(m => m.Id == id, entity);
            return _mapper.Map<PostResponseTo>(entity);
        }
    }
}
