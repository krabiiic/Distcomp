using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.Services.Implementations;

public class MarkService : IMarkService
{
    private readonly IMarkRepository _markRepository;
    private readonly IMapper _mapper;
    private readonly MarkRequestDTOValidator _validator;
    
    public MarkService(IMarkRepository markRepository, 
        IMapper mapper, MarkRequestDTOValidator validator)
    {
        _markRepository = markRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<MarkResponseDTO>> GetMarksAsync()
    {
        var marks = await _markRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<MarkResponseDTO>>(marks);
    }

    public async Task<MarkResponseDTO> GetMarkByIdAsync(long id)
    {
        var mark = await _markRepository.GetByIdAsync(id)
                   ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        return _mapper.Map<MarkResponseDTO>(mark);
    }

    public async Task<MarkResponseDTO> CreateMarkAsync(MarkRequestDTO mark)
    {
        await _validator.ValidateAndThrowAsync(mark);
        var markToCreate = _mapper.Map<Mark>(mark);
        var createdMark = await _markRepository.CreateAsync(markToCreate);
        return _mapper.Map<MarkResponseDTO>(createdMark);
    }

    public async Task<MarkResponseDTO> UpdateMarkAsync(MarkRequestDTO mark)
    {
        await _validator.ValidateAndThrowAsync(mark);
        var markToUpdate = _mapper.Map<Mark>(mark);
        var updatedMark = await _markRepository.UpdateAsync(markToUpdate)
                          ?? throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(mark.Id));
        return _mapper.Map<MarkResponseDTO>(updatedMark);
    }

    public async Task DeleteMarkAsync(long id)
    {
        if (!await _markRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.MarkNotFound, ErrorMessages.MarkNotFoundMessage(id));
        }
    }
}