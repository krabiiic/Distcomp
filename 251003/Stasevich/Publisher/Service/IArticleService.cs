using WebApplication1.DTO;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public interface IArticleService
    {
        Task<ArticleResponseTo> CreateArticleAsync(ArticleRequestTo dto);
        Task<ArticleResponseTo> GetArticleByIdAsync(long id);
        Task<PaginatedResult<ArticleResponseTo>> GetAllArticlesAsync(
            int pageNumber = 1,
            int pageSize = 10,
            string? sortBy = null,
            string? filter = null);
        Task<ArticleResponseTo> UpdateArticleAsync(long id, ArticleRequestTo dto);
        Task DeleteArticleAsync(long id);
    }
}
