using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Exceptions;
using DistComp.Infrastructure.Validators;
using DistComp.Models;
using DistComp.Repositories.Interfaces;
using DistComp.Services.Interfaces;
using FluentValidation;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace DistComp.Services.Implementations;

public class CreatorService : ICreatorService
{
    private readonly ICreatorRepository _creatorRepository;
    private readonly IMapper _mapper;
    private readonly CreatorRequestDTOValidator _validator;
    
    public CreatorService(ICreatorRepository creatorRepository, 
        IMapper mapper, CreatorRequestDTOValidator validator)
    {
        _creatorRepository = creatorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync()
    {
        var creators = await _creatorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<CreatorResponseDTO>>(creators);
    }

    public async Task<CreatorResponseDTO> GetCreatorByIdAsync(long id)
    {
        var creator = await _creatorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        return _mapper.Map<CreatorResponseDTO>(creator);
    }

    public async Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO creator)
    {
        await _validator.ValidateAndThrowAsync(creator);
        var creatorToCreate = _mapper.Map<Creator>(creator);
        var createdCreator = await _creatorRepository.CreateAsync(creatorToCreate);
        return _mapper.Map<CreatorResponseDTO>(createdCreator);
    }

    public async Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO creator)
    {
        await _validator.ValidateAndThrowAsync(creator);
        var creatorToUpdate = _mapper.Map<Creator>(creator);
        var updatedCreator = await _creatorRepository.UpdateAsync(creatorToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(creator.Id));
        return _mapper.Map<CreatorResponseDTO>(updatedCreator);
    }

    public async Task DeleteCreatorAsync(long id)
    {
        if (!await _creatorRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        }
    }
}