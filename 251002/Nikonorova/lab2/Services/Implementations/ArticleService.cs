using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.EntityFrameworkCore;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using lab2_jpa.Exceptions;
using lab2_jpa.Mappers;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.Models;
using System;
namespace lab2_jpa.Services.Implementations
{
    public sealed class ArticleService(AppDbContext context) : IArticleService
    {
        public async Task<ArticleResponseTo> GetArticleById(long id)
        {
            var tweet = await context.Articles.FindAsync(id);
            if (tweet == null) throw new EntityNotFoundException($"Article with id = {id} doesn't exist.");

            return ArticleMapper.Map(tweet);
        }

        public async Task<IEnumerable<ArticleResponseTo>> GetArticles()
        {
            try
            {
                return ArticleMapper.Map(await context.Articles.ToListAsync());
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                return Enumerable.Empty<ArticleResponseTo>();
            }
        }

        public async Task<ArticleResponseTo> CreateArticle(CreateArticleRequestTo createArticleRequestTo)
        {

            var tweet = ArticleMapper.Map(createArticleRequestTo);
            await context.Articles.AddAsync(tweet);
            await context.SaveChangesAsync();
            return ArticleMapper.Map(tweet);
        }

        public async Task DeleteArticle(long id)
        {
            var tweet = await context.Articles.FindAsync(id);
            if (tweet == null) throw new EntityNotFoundException($"Article with id = {id} doesn't exist.");

            context.Articles.Remove(tweet);
            await context.SaveChangesAsync();
        }

        public async Task<ArticleResponseTo> UpdateArticle(UpdateArticleRequestTo modifiedArticle)
        {
            var tweet = await context.Articles.FindAsync(modifiedArticle.id);
            if (tweet == null) throw new EntityNotFoundException($"Article with id = {modifiedArticle.id} doesn't exist.");

            context.Entry(tweet).State = EntityState.Modified;

            tweet.id = modifiedArticle.id;
            tweet.Content = modifiedArticle.Content;
            tweet.creator_id = modifiedArticle.creator_id;
            tweet.Title = modifiedArticle.Title;


            await context.SaveChangesAsync();
            return ArticleMapper.Map(tweet);
        }
    }
}
