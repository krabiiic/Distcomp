using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;

namespace TaskSQL.Services.Interfaces;

public interface ITweetService
{
    Task<TweetResponseTo> GetTweetById(long id);
    Task<IEnumerable<TweetResponseTo>> GetTweets();
    Task<TweetResponseTo> CreateTweet(CreateTweetRequestTo createTweetRequestTo);
    Task DeleteTweet(long id);
    Task<TweetResponseTo> UpdateTweet(UpdateTweetRequestTo modifiedTweet);
}