using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Exceptions;
using DistComp_1.Infrastructure.Validators;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;
using DistComp_1.Services.Interfaces;
using FluentValidation;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace DistComp_1.Services.Implementations;

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
        var users = await _creatorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<CreatorResponseDTO>>(users);
    }

    public async Task<CreatorResponseDTO> GetCreatorByIdAsync(long id)
    {
        var user = await _creatorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        return _mapper.Map<CreatorResponseDTO>(user);
    }

    public async Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO creator)
    {
        await _validator.ValidateAndThrowAsync(creator);
        var creatorToCreate = _mapper.Map<Creator>(creator);
        var createdUser = await _creatorRepository.CreateAsync(creatorToCreate);
        return _mapper.Map<CreatorResponseDTO>(createdUser);
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
        if (await _creatorRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        }
    }
}