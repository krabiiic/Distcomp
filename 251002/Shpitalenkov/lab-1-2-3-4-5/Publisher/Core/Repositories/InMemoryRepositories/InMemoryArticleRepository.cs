using Core.Entities;
using Core.Exceptions;
using Core.Interfaces;

namespace Core.Repositories.InMemoryRepositories;

public class 
    InMemoryArticleRepository : IArticleRepository
{
    private readonly Dictionary<long, Article> _articles = new ();
    private long _nextId = 1;

    public async Task<Article?> AddArticle(Article article)
    {
        article.Id = _nextId;
        _articles.Add(_nextId,article);
        return _articles[_nextId++];
    }

    public async Task<Article?> GetArticle(long articleId)
    {
        if (_articles.TryGetValue(articleId, out Article article))
        {
            return article;
        }
        throw new NotFoundException("Id",$"{articleId}");
    }

    public async Task<Article?> RemoveArticle(long articleId)
    {
        if (_articles.Remove(articleId, out Article article))
        {
            return article;
        }
        throw new NotFoundException("Id",$"{articleId}");
    }

    public async Task<Article?> UpdateArticle(Article article)
    {
        if (_articles.ContainsKey(article.Id))
        {
            article.Modified = DateTime.Now;
            _articles[article.Id] = article;
            return _articles[article.Id];
        }
        throw new NotFoundException("Id",$"{article.Id}");
    }

    public void AddTagsToArticle(long articleId, IEnumerable<long> articleIds)
    {
        throw new NotImplementedException();
    }

    public async Task<IEnumerable<Article?>?> GetAllArticle()
    {
        return _articles.Values.ToList();
    }
}