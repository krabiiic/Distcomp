using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface INewservice
{
    Task<IEnumerable<NewsResponseDTO>> GetStoriesAsync();

    Task<NewsResponseDTO> GetNewsByIdAsync(long id);

    Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news);

    Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news);

    Task DeleteNewsAsync(long id);
}