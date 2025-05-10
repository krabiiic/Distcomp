using Microsoft.EntityFrameworkCore;
using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Publisher.Exceptions;
using Publisher.Mappers;
using Publisher.Models;
using Publisher.Services.Interfaces;
using Publisher.Storage;
namespace Publisher.Services.Implementations;

public sealed class TweetService(AppDbContext context) : ITweetService
{
    public async Task<TweetResponseTo> GetTweetById(long id)
    {
        Tweet? tweet = await context.Tweets.FindAsync(id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {id} doesn't exist.");

        return tweet.ToResponse();
    }

    public async Task<IEnumerable<TweetResponseTo>> GetTweets()
    {
        return (await context.Tweets.ToListAsync()).ToResponse();
    }

    public async Task<TweetResponseTo> CreateTweet(CreateTweetRequestTo createTweetRequestTo)
    {
        Tweet tweet = createTweetRequestTo.ToEntity();
        await context.Tweets.AddAsync(tweet);
        await context.SaveChangesAsync();
        return tweet.ToResponse();
    }

    public async Task DeleteTweet(long id)
    {
        Tweet? tweet = await context.Tweets.FindAsync(id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {id} doesn't exist.");

        context.Tweets.Remove(tweet);
        await context.SaveChangesAsync();
    }

    public async Task<TweetResponseTo> UpdateTweet(UpdateTweetRequestTo modifiedTweet)
    {
        Tweet? tweet = await context.Tweets.FindAsync(modifiedTweet.Id);
        if (tweet == null) throw new EntityNotFoundException($"Tweet with id = {modifiedTweet.Id} doesn't exist.");

        context.Entry(tweet).State = EntityState.Modified;

        tweet.Id = modifiedTweet.Id;
        tweet.Content = modifiedTweet.Content;
        tweet.CreatorId = modifiedTweet.CreatorId;
        tweet.Title = modifiedTweet.Title;

        await context.SaveChangesAsync();
        return tweet.ToResponse();
    }
}
