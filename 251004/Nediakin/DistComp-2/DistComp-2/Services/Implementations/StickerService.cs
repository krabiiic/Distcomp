using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;

namespace DistComp_1.Services.Implementations;

public class StickerService : IStickerService
{
    private readonly IStickerRepository _stickerRepository;
    private readonly IMapper _mapper;
    private readonly StickerRequestDTOValidator _validator;
    
    public StickerService(IStickerRepository stickerRepository, 
        IMapper mapper, StickerRequestDTOValidator validator)
    {
        _stickerRepository = stickerRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<StickerResponseDTO>> GetStickersAsync()
    {
        var stickers = await _stickerRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<StickerResponseDTO>>(stickers);
    }

    public async Task<StickerResponseDTO> GetStickerByIdAsync(long id)
    {
        var sticker = await _stickerRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.StickerNotFound, ErrorMessages.StickerNotFoundMessage(id));
        return _mapper.Map<StickerResponseDTO>(sticker);
    }

    public async Task<StickerResponseDTO> CreateStickerAsync(StickerRequestDTO sticker)
    {
        await _validator.ValidateAndThrowAsync(sticker);
        var stickerToCreate = _mapper.Map<Sticker>(sticker);
        var createdSticker = await _stickerRepository.CreateAsync(stickerToCreate);
        return _mapper.Map<StickerResponseDTO>(createdSticker);
    }

    public async Task<StickerResponseDTO> UpdateStickerAsync(StickerRequestDTO sticker)
    {
        await _validator.ValidateAndThrowAsync(sticker);
        var stickerToUpdate = _mapper.Map<Sticker>(sticker);
        var updatedSticker = await _stickerRepository.UpdateAsync(stickerToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.StickerNotFound, ErrorMessages.StickerNotFoundMessage(sticker.Id));
        return _mapper.Map<StickerResponseDTO>(updatedSticker);
    }

    public async Task DeleteStickerAsync(long id)
    {
        if (await _stickerRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.StickerNotFound, ErrorMessages.StickerNotFoundMessage(id));
        }
    }
}