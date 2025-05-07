using DistComp_1.Exceptions;
using DistComp_1.Models;
using DistComp_1.Repositories.Interfaces;

namespace DistComp_1.Repositories.Implementations;

public class InMemoryArticleRepository : BaseInMemoryRepository<Article>, IArticleRepository
{
    /*
    // Индекс для поиска по title истории
    private readonly Dictionary<string, long> _titleIndex = [];

    public override async Task<Article> CreateAsync(Article entity)
    {
        if (_titleIndex.ContainsKey(entity.Title))
        {
            throw new ConflictException(ErrorCodes.ArticleAlreadyExists, 
                ErrorMessages.ArticleAlreadyExists(entity.Title));
        }

        var Article = await base.CreateAsync(entity);
        _titleIndex.Add(Article.Title, Article.Id);

        return Article;
    }

    public override async Task<Article?> UpdateAsync(Article entity)
    {
        if (_titleIndex.TryGetValue(entity.Title, out long value) && value != entity.Id)
        {
            throw new ConflictException(ErrorCodes.ArticleAlreadyExists, 
                ErrorMessages.ArticleAlreadyExists(entity.Title));
        }

        var updatedArticle = await base.UpdateAsync(entity);
        if (updatedArticle != null)
        {
            if (_titleIndex.ContainsKey(entity.Title) && _titleIndex[entity.Title] == entity.Id)
            {
                return updatedArticle;
            }

            _titleIndex.Remove(entity.Title);
            _titleIndex.Add(updatedArticle.Title, updatedArticle.Id);
        }

        return updatedArticle;
    }

    public override async Task<Article?> DeleteAsync(long id)
    {
        var Article = await base.DeleteAsync(id);

        if (Article != null)
        {
            _titleIndex.Remove(Article.Title);
        }

        return Article;
    }
    */
}