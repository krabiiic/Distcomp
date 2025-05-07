using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.Services.Interfaces;

public interface IArticleService
{
    Task<IEnumerable<ArticleResponseDTO>> GetStoriesAsync();

    Task<ArticleResponseDTO> GetArticleByIdAsync(long id);

    Task<ArticleResponseDTO> CreateArticleAsync(ArticleRequestDTO article);

    Task<ArticleResponseDTO> UpdateArticleAsync(ArticleRequestDTO article);

    Task DeleteArticleAsync(long id);
}