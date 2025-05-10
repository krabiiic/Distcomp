namespace LAB1.Services;

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

    public async Task<IEnumerable<StickerResponseTo>> GetAllAsync()
    {
        var stickers = await _repository.GetAllAsync();
        return stickers.Select(s => new StickerResponseTo
        {
            Id = s.Id,
            Name = s.Name
        });
    }

    public async Task<StickerResponseTo> GetByIdAsync(int id)
    {
        var sticker = await _repository.GetByIdAsync(id);
        if (sticker == null) return null;

        return new StickerResponseTo
        {
            Id = sticker.Id,
            Name = sticker.Name
        };
    }

    public async Task<StickerResponseTo> CreateAsync(StickerRequestTo stickerRequest)
    {
        var sticker = new Sticker
        {
            Name = stickerRequest.Name
        };

        await _repository.CreateAsync(sticker);
        return new StickerResponseTo
        {
            Id = sticker.Id,
            Name = sticker.Name
        };
    }

    public async Task<StickerResponseTo> UpdateAsync(int id, StickerRequestTo stickerRequest)
    {
        var sticker = await _repository.GetByIdAsync(id);
        if (sticker == null) return null;

        sticker.Name = stickerRequest.Name;

        await _repository.UpdateAsync(sticker);
        return new StickerResponseTo
        {
            Id = sticker.Id,
            Name = sticker.Name
        };
    }

    public async Task<bool> DeleteAsync(int id)
    {
        return await _repository.DeleteAsync(id);
    }
}