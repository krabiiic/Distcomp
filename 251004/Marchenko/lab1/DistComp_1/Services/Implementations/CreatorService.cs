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
    private readonly ICreatorRepository _CreatorRepository;
    private readonly IMapper _mapper;
    private readonly CreatorRequestDTOValidator _validator;
    
    public CreatorService(ICreatorRepository CreatorRepository, 
        IMapper mapper, CreatorRequestDTOValidator validator)
    {
        _CreatorRepository = CreatorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<CreatorResponseDTO>> GetCreatorsAsync()
    {
        var Creators = await _CreatorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<CreatorResponseDTO>>(Creators);
    }

    public async Task<CreatorResponseDTO> GetCreatorByIdAsync(long id)
    {
        var Creator = await _CreatorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        return _mapper.Map<CreatorResponseDTO>(Creator);
    }

    public async Task<CreatorResponseDTO> CreateCreatorAsync(CreatorRequestDTO Creator)
    {
        await _validator.ValidateAndThrowAsync(Creator);
        var CreatorToCreate = _mapper.Map<Creator>(Creator);
        var createdCreator = await _CreatorRepository.CreateAsync(CreatorToCreate);
        return _mapper.Map<CreatorResponseDTO>(createdCreator);
    }

    public async Task<CreatorResponseDTO> UpdateCreatorAsync(CreatorRequestDTO Creator)
    {
        await _validator.ValidateAndThrowAsync(Creator);
        var CreatorToUpdate = _mapper.Map<Creator>(Creator);
        var updatedCreator = await _CreatorRepository.UpdateAsync(CreatorToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(Creator.Id));
        return _mapper.Map<CreatorResponseDTO>(updatedCreator);
    }

    public async Task DeleteCreatorAsync(long id)
    {
        if (await _CreatorRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.CreatorNotFound, ErrorMessages.CreatorNotFoundMessage(id));
        }
    }
}