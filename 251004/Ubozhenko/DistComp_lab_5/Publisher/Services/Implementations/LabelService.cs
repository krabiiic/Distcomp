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

public class LabelService : ILabelService
{
    private readonly ILabelRepository _tagRepository;
    private readonly IMapper _mapper;
    private readonly LabelRequestDTOValidator _validator;
    
    public LabelService(ILabelRepository tagRepository, 
        IMapper mapper, LabelRequestDTOValidator validator)
    {
        _tagRepository = tagRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<LabelResponseDTO>> GetLabelsAsync()
    {
        var tags = await _tagRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<LabelResponseDTO>>(tags);
    }

    public async Task<LabelResponseDTO> GetLabelByIdAsync(long id)
    {
        var tag = await _tagRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(id));
        return _mapper.Map<LabelResponseDTO>(tag);
    }

    public async Task<LabelResponseDTO> CreateLabelAsync(LabelRequestDTO label)
    {
        await _validator.ValidateAndThrowAsync(label);
        var tagToCreate = _mapper.Map<Label>(label);
        var createdLabel = await _tagRepository.CreateAsync(tagToCreate);
        return _mapper.Map<LabelResponseDTO>(createdLabel);
    }

    public async Task<LabelResponseDTO> UpdateLabelAsync(LabelRequestDTO label)
    {
        await _validator.ValidateAndThrowAsync(label);
        var tagToUpdate = _mapper.Map<Label>(label);
        var updatedLabel = await _tagRepository.UpdateAsync(tagToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(label.Id));
        return _mapper.Map<LabelResponseDTO>(updatedLabel);
    }

    public async Task DeleteLabelAsync(long id)
    {
        if (!await _tagRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(id));
        }
    }
}