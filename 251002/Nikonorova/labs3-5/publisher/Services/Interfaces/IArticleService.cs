using publisher.DTO.Request;
using publisher.DTO.Response;

namespace publisher.Services.Interfaces
{
    public interface IArticleService
    {
        Task<ArticleResponseTo> GetArticleById(long id);
        Task<IEnumerable<ArticleResponseTo>> GetArticles();
        Task<ArticleResponseTo> CreateArticle(CreateArticleRequestTo createArticleRequestTo);
        Task DeleteArticle(long id);
        Task<ArticleResponseTo> UpdateArticle(UpdateArticleRequestTo modifiedArticle);
    }
}
