using Core.DTO;
using Core.Interfaces;
using AutoMapper;
using Core.Entities;
using Core.Exceptions;

namespace Core.Services;

public class ArticleService(IArticleRepository _articleRepository, IMapper _mapper) : IArticleService
{
    public async Task<ArticleResponseToGetById?> CreateArticle(ArticleRequestToCreate model, IEnumerable<long>? tagIds)
    {
        Article article = _mapper.Map<Article>(model);
        
        Validate(article);
        
        article = await _articleRepository.AddArticle(article);
        if (tagIds != null)
        {
            _articleRepository.AddTagsToArticle(article.Id, tagIds);
        }
        return _mapper.Map<ArticleResponseToGetById>(article);
    }

    public async Task<IEnumerable<ArticleResponseToGetById?>?> GetArticle(ArticleRequestToGetAll request)
    {
        var article = await _articleRepository.GetAllArticle();
        return article.Select(_mapper.Map<ArticleResponseToGetById>);
    }

    public async Task<ArticleResponseToGetById?> GetArticleById(ArticleRequestToGetById request)
    {
        var article = await _articleRepository.GetArticle(request.Id);
        return _mapper.Map<ArticleResponseToGetById>(article);
    }

    public async Task<ArticleResponseToGetById?> UpdateArticle(ArticleRequestToFullUpdate model)
    {
        var article = _mapper.Map<Article>(model);
        Validate(article);
        article = await _articleRepository.UpdateArticle(article);
        return _mapper.Map<ArticleResponseToGetById>(article);
    }

    public async Task<ArticleResponseToGetById?> DeleteArticle(ArticleRequestToDeleteById request)
    {
        var article = await _articleRepository.RemoveArticle(request.Id);
        return _mapper.Map<ArticleResponseToGetById>(article);
    }
    
    private bool Validate(Article article)
    {
        var errors = new Dictionary<string, string[]>();
        if (article.Title.Length < 2 || article.Title.Length > 64)
        {
            errors.Add("Title",["Should be from 2 to 64 chars"]);
        }
        if (article.Content.Length < 4 || article.Content.Length > 2048)
        {
            errors.Add("Content",["Should be from 2 to 64 chars"]);
        }
        if (errors.Count != 0)
        {
            throw new BadRequestException("Validation error", errors);
        }
        return true;
    }
}