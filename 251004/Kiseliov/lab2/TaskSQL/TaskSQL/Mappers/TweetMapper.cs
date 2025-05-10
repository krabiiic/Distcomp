using Riok.Mapperly.Abstractions;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;

namespace TaskSQL.Mappers;

[Mapper]
public static partial class TweetMapper
{
    public static partial Tweet Map(UpdateTweetRequestTo updateTweetRequestTo);
    public static partial Tweet Map(CreateTweetRequestTo createTweetRequestTo);
    public static partial TweetResponseTo Map(Tweet tweet);
    public static partial IEnumerable<TweetResponseTo> Map(IEnumerable<Tweet> tweets);

    public static partial IEnumerable<Tweet> Map(
        IEnumerable<UpdateTweetRequestTo> tweetRequestTos);
}