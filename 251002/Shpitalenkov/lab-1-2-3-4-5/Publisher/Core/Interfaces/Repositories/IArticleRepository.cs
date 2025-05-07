using Core.Entities;

namespace Core.Interfaces;

public interface IArticleRepository
{
    Task<Article?> AddArticle(Article article);
    Task<Article?> GetArticle(long articleId);
    Task<Article?> RemoveArticle(long articleId);
    Task<Article?> UpdateArticle(Article article);

    void AddTagsToArticle(long articleId, IEnumerable<long> articleIds);
    Task<IEnumerable<Article?>?> GetAllArticle();
}