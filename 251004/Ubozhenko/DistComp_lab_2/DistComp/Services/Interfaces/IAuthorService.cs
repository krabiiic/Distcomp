using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;

namespace DistComp.Services.Interfaces;

public interface IAuthorService
{
    Task<IEnumerable<AuthorResponseDTO>> GetAuthorsAsync();

    Task<AuthorResponseDTO> GetAuthorByIdAsync(long id);

    Task<AuthorResponseDTO> CreateAuthorAsync(AuthorRequestDTO author);

    Task<AuthorResponseDTO> UpdateAuthorAsync(AuthorRequestDTO author);

    Task DeleteAuthorAsync(long id);
}