using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface INewsService
{
    Task<IEnumerable<NewsResponseDTO>> GetNewsAsync();

    Task<NewsResponseDTO> GetNewsByIdAsync(long id);

    Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news);

    Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news);

    Task DeleteNewsAsync(long id);
}