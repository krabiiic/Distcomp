using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class IssueService : IIssueService
    {
        private IIssueRepository _repository;
        private IValidator<Issue> _validator;
        public IssueService(IIssueRepository repository, IValidator<Issue> validator) => (_repository, _validator) = (repository, validator);
        public IssueResponseTo CreateIssue(Issue creator)
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
        public bool DeleteIssue(ulong id)
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
        public IssueResponseTo? GetIssue(ulong id)
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
        public List<IssueResponseTo> GetAllIssues()
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
        public IssueResponseTo? UpdateIssue(Issue creator, ulong id)
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
