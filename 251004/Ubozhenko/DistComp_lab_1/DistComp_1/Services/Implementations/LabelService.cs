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

public class LabelService : ILabelService
{
    private readonly ILabelRepository _labelRepository;
    private readonly IMapper _mapper;
    private readonly LabelRequestDTOValidator _validator;
    
    public LabelService(ILabelRepository labelRepository, 
        IMapper mapper, LabelRequestDTOValidator validator)
    {
        _labelRepository = labelRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<LabelResponseDTO>> GetLabelsAsync()
    {
        var labels = await _labelRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<LabelResponseDTO>>(labels);
    }

    public async Task<LabelResponseDTO> GetLabelByIdAsync(long id)
    {
        var label = await _labelRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(id));
        return _mapper.Map<LabelResponseDTO>(label);
    }

    public async Task<LabelResponseDTO> CreateLabelAsync(LabelRequestDTO label)
    {
        await _validator.ValidateAndThrowAsync(label);
        var labelToCreate = _mapper.Map<Label>(label);
        var createdLabel = await _labelRepository.CreateAsync(labelToCreate);
        return _mapper.Map<LabelResponseDTO>(createdLabel);
    }

    public async Task<LabelResponseDTO> UpdateLabelAsync(LabelRequestDTO label)
    {
        await _validator.ValidateAndThrowAsync(label);
        var labelToUpdate = _mapper.Map<Label>(label);
        var updatedLabel = await _labelRepository.UpdateAsync(labelToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(label.Id));
        return _mapper.Map<LabelResponseDTO>(updatedLabel);
    }

    public async Task DeleteLabelAsync(long id)
    {
        if (await _labelRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.LabelNotFound, ErrorMessages.LabelNotFoundMessage(id));
        }
    }
}