using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IAuthorService
{
    Task<IEnumerable<AuthorResponseDTO>> GetAuthorsAsync();

    Task<AuthorResponseDTO> GetAuthorByIdAsync(long id);

    Task<AuthorResponseDTO> CreateAuthorAsync(AuthorRequestDTO author);

    Task<AuthorResponseDTO> UpdateAuthorAsync(AuthorRequestDTO author);

    Task DeleteAuthorAsync(long id);
}