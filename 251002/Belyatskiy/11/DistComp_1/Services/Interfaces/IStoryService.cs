using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;

namespace DistComp_1.Services.Interfaces;

public interface IArticleService
{
    Task<IEnumerable<ArticleResponseDTO>> GetStoriesAsync();

    Task<ArticleResponseDTO> GetArticleByIdAsync(long id);

    Task<ArticleResponseDTO> CreateArticleAsync(ArticleRequestDTO Article);

    Task<ArticleResponseDTO> UpdateArticleAsync(ArticleRequestDTO Article);

    Task DeleteArticleAsync(long id);
}