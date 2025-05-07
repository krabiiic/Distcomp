using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Services.Interfaces
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
