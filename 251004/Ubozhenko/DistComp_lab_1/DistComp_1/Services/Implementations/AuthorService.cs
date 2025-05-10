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

public class AuthorService : IAuthorService
{
    private readonly IAuthorRepository _authorRepository;
    private readonly IMapper _mapper;
    private readonly AuthorRequestDTOValidator _validator;
    
    public AuthorService(IAuthorRepository authorRepository, 
        IMapper mapper, AuthorRequestDTOValidator validator)
    {
        _authorRepository = authorRepository;
        _mapper = mapper;
        _validator = validator;
    }
    
    public async Task<IEnumerable<AuthorResponseDTO>> GetAuthorsAsync()
    {
        var authors = await _authorRepository.GetAllAsync();
        return _mapper.Map<IEnumerable<AuthorResponseDTO>>(authors);
    }

    public async Task<AuthorResponseDTO> GetAuthorByIdAsync(long id)
    {
        var author = await _authorRepository.GetByIdAsync(id)
                      ?? throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(id));
        return _mapper.Map<AuthorResponseDTO>(author);
    }

    public async Task<AuthorResponseDTO> CreateAuthorAsync(AuthorRequestDTO author)
    {
        await _validator.ValidateAndThrowAsync(author);
        var authorToCreate = _mapper.Map<Author>(author);
        var createdAuthor = await _authorRepository.CreateAsync(authorToCreate);
        return _mapper.Map<AuthorResponseDTO>(createdAuthor);
    }

    public async Task<AuthorResponseDTO> UpdateAuthorAsync(AuthorRequestDTO author)
    {
        await _validator.ValidateAndThrowAsync(author);
        var authorToUpdate = _mapper.Map<Author>(author);
        var updatedAuthor = await _authorRepository.UpdateAsync(authorToUpdate)
                             ?? throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(author.Id));
        return _mapper.Map<AuthorResponseDTO>(updatedAuthor);
    }

    public async Task DeleteAuthorAsync(long id)
    {
        if (await _authorRepository.DeleteAsync(id) is null)
        {
            throw new NotFoundException(ErrorCodes.AuthorNotFound, ErrorMessages.AuthorNotFoundMessage(id));
        }
    }
}
