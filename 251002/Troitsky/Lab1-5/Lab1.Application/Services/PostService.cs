// Было нужно для 1-3 лабораторных

/*
using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class PostService : IPostService
    {
        private IPostRepository _repository;
        private IValidator<Post> _validator;
        public PostService(IPostRepository repository, IValidator<Post> validator) => (_repository, _validator) = (repository, validator);
        public PostResponseTo CreatePost(Post post)
        {
            var res = _validator.Validate(post);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Create(post);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public bool DeletePost(ulong id)
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
        public PostResponseTo? GetPost(ulong id)
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
        public List<PostResponseTo> GetAllPosts()
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
        public PostResponseTo? UpdatePost(Post post, ulong id)
        {
            var res = _validator.Validate(post);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Update(post, id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
    }
}
*/