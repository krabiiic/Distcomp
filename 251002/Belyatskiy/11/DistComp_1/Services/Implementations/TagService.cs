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

public class MarkService : IMarkService
{
    private readonly IMarkRepository _MarkRepository;
    private readonly IMapper _mapper;
    private readonly MarkRequestDTOValidator _validator;
    
    public MarkService(IMarkRepository MarkRepository, 
        IMapper mapper, MarkRequestDTOValidator validator)
    {
        _MarkRepository = MarkRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<MarkResponseDTO>> GetMarksAsync()
    {
        var Marks = await _MarkRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<MarkResponseDTO>>(Marks);
    }

    public async Task<MarkResponseDTO> GetMarkByIdAsync(long id)
    {
        var Mark = await _MarkRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        return _mapper.Map<MarkResponseDTO>(Mark);
    }

    public async Task<MarkResponseDTO> CreateMarkAsync(MarkRequestDTO Mark)
    {
        await _validator.ValidateAndThrowAsync(Mark);
        var MarkToCreate = _mapper.Map<Mark>(Mark);
        var createdMark = await _MarkRepository.CreateAsync(MarkToCreate);
        return _mapper.Map<MarkResponseDTO>(createdMark);
    }

    public async Task<MarkResponseDTO> UpdateMarkAsync(MarkRequestDTO Mark)
    {
        await _validator.ValidateAndThrowAsync(Mark);
        var MarkToUpdate = _mapper.Map<Mark>(Mark);
        var updatedMark = await _MarkRepository.UpdateAsync(MarkToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(Mark.Id));
        return _mapper.Map<MarkResponseDTO>(updatedMark);
    }

    public async Task DeleteMarkAsync(long id)
    {
        if (await _MarkRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        }
    }
}