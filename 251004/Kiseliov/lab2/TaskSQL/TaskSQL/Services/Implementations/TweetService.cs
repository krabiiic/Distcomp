using Microsoft.EntityFrameworkCore;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Exceptions;
using TaskSQL.Models;
using TaskSQL.Services.Interfaces;
using TaskSQL.Storage;
using TweetMapper = TaskSQL.Mappers.TweetMapper;

namespace TaskSQL.Services.Implementations;

public sealed class TweetService(AppDbContext context) : ITweetService
{
    public async Task<TweetResponseTo> GetTweetById(long id)
    {
        var tweet = await context.Tweets.FindAsync(id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {id} doesn't exist.");

        return TweetMapper.Map(tweet);
    }

    public async Task<IEnumerable<TweetResponseTo>> GetTweets()
    {
        try
        {
            return TweetMapper.Map(await context.Tweets.ToListAsync());
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex);
            return Enumerable.Empty<TweetResponseTo>();
        }
    }

    public async Task<TweetResponseTo> CreateTweet(CreateTweetRequestTo createTweetRequestTo)
    {
        
        var tweet = TweetMapper.Map(createTweetRequestTo);
        await context.Tweets.AddAsync(tweet);
        await context.SaveChangesAsync();
        return TweetMapper.Map(tweet);
    }

    public async Task DeleteTweet(long id)
    {
        var tweet = await context.Tweets.FindAsync(id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {id} doesn't exist.");

        context.Tweets.Remove(tweet);
        await context.SaveChangesAsync();
    }

    public async Task<TweetResponseTo> UpdateTweet(UpdateTweetRequestTo modifiedTweet)
    {
        var tweet = await context.Tweets.FindAsync(modifiedTweet.id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {modifiedTweet.id} doesn't exist.");

        context.Entry(tweet).State = EntityState.Modified;

        tweet.id = modifiedTweet.id;
        tweet.Content = modifiedTweet.Content;
        tweet.creator_id = modifiedTweet.creator_id;
        tweet.Title = modifiedTweet.Title;


        await context.SaveChangesAsync();
        return TweetMapper.Map(tweet);
    }
}