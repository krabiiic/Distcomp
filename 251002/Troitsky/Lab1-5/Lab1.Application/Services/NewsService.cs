using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class NewsService : INewsService
    {
        private INewsRepository _repository;
        private IValidator<News> _validator;
        public NewsService(INewsRepository repository, IValidator<News> validator) => (_repository, _validator) = (repository, validator);
        public NewsResponseTo CreateNews(News news)
        {
            var res = _validator.Validate(news);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Create(news);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public bool DeleteNews(ulong id)
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
        public NewsResponseTo? GetNews(ulong id)
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
        public List<NewsResponseTo> GetAllNews()
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
        public NewsResponseTo? UpdateNews(News news, ulong id)
        {
            var res = _validator.Validate(news);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Update(news, id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
    }
}
