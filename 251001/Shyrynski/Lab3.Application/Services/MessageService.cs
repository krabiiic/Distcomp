using Lab3.Core.Abstractions;
using Lab3.Core.Models;
using Lab3.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab3.Application.Exceptions;

namespace Lab3.Application.Services
{
    public class MessageService : IMessageService
    {
        private IMessageRepository _repository;
        private IValidator<Message> _validator;
        public MessageService(IMessageRepository repository, IValidator<Message> validator) => (_repository, _validator) = (repository, validator);
        public MessageResponseTo CreateMessage(Message creator)
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
        public bool DeleteMessage(ulong id)
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
        public MessageResponseTo? GetMessage(ulong id)
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
        public List<MessageResponseTo> GetAllMessages()
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
        public MessageResponseTo? UpdateMessage(Message creator, ulong id)
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
