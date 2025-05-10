using Microsoft.EntityFrameworkCore;
using static System.Net.Mime.MediaTypeNames;
using System;
using publisher.DTO.Response;
using publisher.Models;
using publisher.DTO.Request;
using publisher.Storage;
using publisher.Services.Interfaces;
using publisher.Exceptions;
using publisher.Mappers;

namespace publisher.Services.Implementations
{
    public sealed class ArticleService(AppDbContext context) : IArticleService
    {
        public async Task<ArticleResponseTo> GetArticleById(long id)
        {
            Article? article = await context.Articles.FindAsync(id);
            if (article == null) throw new EntityNotFoundException($"Article with id = {id} doesn't exist.");

            return article.ToResponse();
        }

        public async Task<IEnumerable<ArticleResponseTo>> GetArticles()
        {
            return (await context.Articles.ToListAsync()).ToResponse();
        }

        public async Task<ArticleResponseTo> CreateArticle(CreateArticleRequestTo createArticleRequestTo)
        {
            Article article = createArticleRequestTo.ToEntity();
            await context.Articles.AddAsync(article);
            await context.SaveChangesAsync();
            return article.ToResponse();
        }

        public async Task DeleteArticle(long id)
        {
            Article? article = await context.Articles.FindAsync(id);
            if (article == null) throw new EntityNotFoundException($"Article with id = {id} doesn't exist.");

            context.Articles.Remove(article);
            await context.SaveChangesAsync();
        }

        public async Task<ArticleResponseTo> UpdateArticle(UpdateArticleRequestTo modifiedArticle)
        {
            Article? article = await context.Articles.FindAsync(modifiedArticle.Id);
            if (article == null) throw new EntityNotFoundException($"Article with id = {modifiedArticle.Id} doesn't exist.");

            context.Entry(article).State = EntityState.Modified;

            article.Id = modifiedArticle.Id;
            article.Content = modifiedArticle.Content;
            article.CreatorId = modifiedArticle.CreatorId;
            article.Title = modifiedArticle.Title;

            await context.SaveChangesAsync();
            return article.ToResponse();
        }
    }
}
