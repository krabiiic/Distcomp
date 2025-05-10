//using AutoMapper;
//using Lab1.Core.Abstractions;
//using Lab1.Core.Contracts;
//using Lab1.Core.Models;
//using Lab1.Infrastructure.Contexts;
//using Lab1.Infrastructure.Entities;
//using Microsoft.EntityFrameworkCore;
//namespace Lab1.Infrastructure.Repositories
//{
//    public class PostRepository : IPostRepository
//    {
//        private DataContext _database;
//        private static ulong _currId = 1;
//        private IMapper _mapper;
//        public PostRepository(DataContext database, IMapper mapper) => (_database, _mapper) = (database, mapper);
//        public PostResponseTo? Get(ulong id)
//        {
//            return _mapper.Map<PostResponseTo>(_database.Posts.AsNoTracking().FirstOrDefault(m => m.Id == id));
//        }
//        public PostResponseTo Create(Post msg)
//        {
//            var postEntity = _mapper.Map<PostEntity>(msg);
//            postEntity.Id = _currId;
//            _database.Posts.Add(postEntity);
//            _database.SaveChanges();
//            return _mapper.Map<PostResponseTo>(postEntity);
//        }
//        public List<PostResponseTo> GetAll()
//        {
//            var msgs = _database.Posts.AsNoTracking().ToList();
//            return _mapper.Map<List<PostResponseTo>>(msgs);
//        }
//        public bool Delete(ulong id)
//        {
//            var entity = _database.Posts.FirstOrDefault(m => m.Id == id);
//            if (entity == null) return false;
//            _database.Posts.Remove(entity);
//            _database.SaveChanges();
//            return true;
//        }
//        public PostResponseTo? Update(Post msg, ulong id)
//        {
//            var entity = _database.Posts.FirstOrDefault(m => m.Id == id);
//            if (entity == null) return null;
//            _mapper.Map<Post, PostEntity>(msg, entity);
//            _database.Posts.Update(entity);
//            _database.SaveChanges();
//            return _mapper.Map<PostResponseTo>(entity);
//        }
//    }
//}
