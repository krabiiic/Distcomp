using AutoMapper;
using FluentValidation;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.Infrastructure.Validators;
using Publisher.Models;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;
using ValidationException = System.ComponentModel.DataAnnotations.ValidationException;

namespace Publisher.Services.Implementations;

public class AuthorService : IAuthorService
{
    private readonly IAuthorRepository _authorRepository;
    private readonly IMapper _mapper;
    private readonly UserRequestDTOValidator _validator;
    
    public AuthorService(IAuthorRepository authorRepository, 
        IMapper mapper, UserRequestDTOValidator validator)
    {
        _authorRepository = authorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<AuthorResponseDTO>> GetAuthorsAsync()
    {
        var users = await _authorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<AuthorResponseDTO>>(users);
    }

    public async Task<AuthorResponseDTO> GetAuthorByIdAsync(long id)
    {
        var user = await _authorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(id));
        return _mapper.Map<AuthorResponseDTO>(user);
    }

    public async Task<AuthorResponseDTO> CreateAuthorAsync(AuthorRequestDTO author)
    {
        await _validator.ValidateAndThrowAsync(author);
        var userToCreate = _mapper.Map<Author>(author);
        var createdUser = await _authorRepository.CreateAsync(userToCreate);
        return _mapper.Map<AuthorResponseDTO>(createdUser);
    }

    public async Task<AuthorResponseDTO> UpdateAuthorAsync(AuthorRequestDTO author)
    {
        await _validator.ValidateAndThrowAsync(author);
        var userToUpdate = _mapper.Map<Author>(author);
        var updatedUser = await _authorRepository.UpdateAsync(userToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(author.Id));
        return _mapper.Map<AuthorResponseDTO>(updatedUser);
    }

    public async Task DeleteAuthorAsync(long id)
    {
        if (!await _authorRepository.DeleteAsync(id))
        {
            throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(id));
        }
    }
}