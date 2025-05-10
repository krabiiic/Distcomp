using Lab1.Core.Abstractions;
using Lab1.Core.Models;
using Lab1.Core.Contracts;
using System.Numerics;
using FluentValidation;
using Lab1.Application.Exceptions;
namespace Lab1.Application.Services
{
    public class StickerService : IStickerService
    {
        private IStickerRepository _repository;
        private IValidator<Sticker> _validator;
        public StickerService(IStickerRepository repository, IValidator<Sticker> validator) => (_repository, _validator) = (repository, validator);
        public StickerResponseTo CreateSticker(Sticker creator)
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
        public bool DeleteSticker(ulong id)
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
        public StickerResponseTo? GetSticker(ulong id)
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
        public List<StickerResponseTo> GetAllStickers()
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
        public StickerResponseTo? UpdateSticker(Sticker creator, ulong id)
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
