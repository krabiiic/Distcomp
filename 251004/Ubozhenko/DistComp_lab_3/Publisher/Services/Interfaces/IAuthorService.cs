using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface IAuthorService
{
    Task<IEnumerable<AuthorResponseDTO>> GetAuthorsAsync();

    Task<AuthorResponseDTO> GetAuthorByIdAsync(long id);

    Task<AuthorResponseDTO> CreateAuthorAsync(AuthorRequestDTO author);

    Task<AuthorResponseDTO> UpdateAuthorAsync(AuthorRequestDTO author);

    Task DeleteAuthorAsync(long id);
}