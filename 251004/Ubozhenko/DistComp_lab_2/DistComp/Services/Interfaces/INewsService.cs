using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using NewsRequestDTO = DistComp.DTO.ResponseDTO.NewsRequestDTO;

namespace DistComp.Services.Interfaces;

public interface INewsService
{
    Task<IEnumerable<NewsRequestDTO>> GetNewsAsync();

    Task<NewsRequestDTO> GetNewsByIdAsync(long id);

    Task<NewsRequestDTO> CreateNewsAsync(DTO.RequestDTO.NewsRequestDTO news);

    Task<NewsRequestDTO> UpdateNewsAsync(DTO.RequestDTO.NewsRequestDTO news);

    Task DeleteNewsAsync(long id);
}