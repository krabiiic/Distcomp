using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class TagService : ITagService
    {
        private ITagRepository _repository;
        private IValidator<Tag> _validator;
        public TagService(ITagRepository repository, IValidator<Tag> validator) => (_repository, _validator) = (repository, validator);
        public TagResponseTo CreateTag(Tag tag)
        {
            var res = _validator.Validate(tag);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Create(tag);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
        public bool DeleteTag(ulong id)
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
        public TagResponseTo? GetTag(ulong id)
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
        public List<TagResponseTo> GetAllTags()
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
        public TagResponseTo? UpdateTag(Tag tag, ulong id)
        {
            var res = _validator.Validate(tag);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Update(tag, id);
            }
            catch
            {
                throw new IncorrectDatabaseException("DB error");
            }
        }
    }
}
