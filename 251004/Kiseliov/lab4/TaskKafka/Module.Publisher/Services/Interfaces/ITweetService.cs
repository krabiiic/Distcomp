using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
namespace Publisher.Services.Interfaces;

public interface ITweetService
{
    Task<TweetResponseTo> GetTweetById(long id);
    Task<IEnumerable<TweetResponseTo>> GetTweets();
    Task<TweetResponseTo> CreateTweet(CreateTweetRequestTo createTweetRequestTo);
    Task DeleteTweet(long id);
    Task<TweetResponseTo> UpdateTweet(UpdateTweetRequestTo modifiedTweet);
}