using Core.DTO;

namespace Core.Interfaces;

public interface IArticleService
{
    public Task<ArticleResponseToGetById?> CreateArticle(ArticleRequestToCreate model, IEnumerable<long>? tagIds);
    public Task<IEnumerable<ArticleResponseToGetById?>?> GetArticle(ArticleRequestToGetAll request);
    public Task<ArticleResponseToGetById?> GetArticleById(ArticleRequestToGetById request);
    public Task<ArticleResponseToGetById?> UpdateArticle(ArticleRequestToFullUpdate model);
    public Task<ArticleResponseToGetById?> DeleteArticle(ArticleRequestToDeleteById request);
}