using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class CreatorService : ICreatorService
    {
        private ICreatorRepository _repository;
        private IValidator<Creator> _validator;
        public CreatorService(ICreatorRepository repository, IValidator<Creator> validator) => (_repository, _validator) = (repository, validator);
        public CreatorResponseTo CreateCreator(Creator creator)
        {
            var res = _validator.Validate(creator);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Create(creator);
            }
            catch
            {
                throw new IncorrectDatabaseException("sdsd");
            }
        }
        public bool DeleteCreator(ulong id)
        {
            try
            {
                return _repository.Delete(id);
            }
            catch
            {
                throw new IncorrectDatabaseException("sdsd");
            }
        }
        public CreatorResponseTo? GetCreator(ulong id)
        {
            try
            {
                return _repository.Get(id);
            }
            catch
            {
                throw new IncorrectDatabaseException("sdsd");
            }
        }
        public List<CreatorResponseTo> GetAllCreators()
        {
            try
            {
                return _repository.GetAll();
            }
            catch
            {
                throw new IncorrectDatabaseException("sdsd");
            }
        }
        public CreatorResponseTo? UpdateCreator(Creator creator, ulong id)
        {
            var res = _validator.Validate(creator);
            if (!res.IsValid)
                throw new IncorrectDataException("invalid input");
            try
            {
                return _repository.Update(creator, id);
            }
            catch
            {
                throw new IncorrectDatabaseException("sdsd");
            }
        }
    }
}
