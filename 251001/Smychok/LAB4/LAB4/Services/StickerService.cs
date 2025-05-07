namespace LAB4.Services;

using DTOs;
using Repositories;
using Models;


public class StickerService
{
    private readonly IRepository<Sticker> _repository;

    public StickerService(IRepository<Sticker> repository)
    {
        _repository = repository;
    }

    public async Task<IEnumerable<StickerResponseTo>> GetAllAsync(QueryParams? queryParams = null)
    {
        var stickers = await _repository.GetAllAsync(queryParams);
        return stickers.Select(s => new StickerResponseTo
        {
            Id = s.id,
            Name = s.name
        });
    }

    public async Task<StickerResponseTo> GetByIdAsync(int id)
    {
        var sticker = await _repository.GetByIdAsync(id);
        if (sticker == null) return null;

        return new StickerResponseTo
        {
            Id = sticker.id,
            Name = sticker.name
        };
    }

    public async Task<StickerResponseTo> CreateAsync(StickerRequestTo stickerRequest)
    {
        if (string.IsNullOrEmpty(stickerRequest.Name))
        {
            throw new ArgumentException("Name cannot be empty.");
        }

        if (stickerRequest.Name.Length > 32)
        {
            throw new ArgumentException("Name must be less than 32 characters.");
        }

        if (stickerRequest.Name.Length < 2)
        {
            throw new ArgumentException("Name must be less than 32 characters.");
        }
        
        var sticker = new Sticker
        {
            name = stickerRequest.Name
        };

        await _repository.CreateAsync(sticker);
        return new StickerResponseTo
        {
            Id = sticker.id,
            Name = sticker.name
        };
    }

    public async Task<StickerResponseTo> UpdateAsync(StickerRequestTo stickerRequest)
    {
        if (string.IsNullOrEmpty(stickerRequest.Name))
        {
            throw new ArgumentException("Name cannot be empty.");
        }

        if (stickerRequest.Name.Length > 32)
        {
            throw new ArgumentException("Name must be less than 32 characters.");
        }

        if (stickerRequest.Name.Length < 2)
        {
            throw new ArgumentException("Name must be less than 32 characters.");
        }
        
        var sticker = await _repository.GetByIdAsync(stickerRequest.id);
        if (sticker == null) return null;

        sticker.name = stickerRequest.Name;

        await _repository.UpdateAsync(sticker);
        return new StickerResponseTo
        {
            Id = sticker.id,
            Name = sticker.name
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}