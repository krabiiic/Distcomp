using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface INewsService
{
    Task<IEnumerable<NewsResponseDTO>> GetNewsAsync();

    Task<NewsResponseDTO> GetNewsByIdAsync(long id);

    Task<NewsResponseDTO> CreateNewsAsync(NewsRequestDTO news);

    Task<NewsResponseDTO> UpdateNewsAsync(NewsRequestDTO news);

    Task DeleteNewsAsync(long id);
}