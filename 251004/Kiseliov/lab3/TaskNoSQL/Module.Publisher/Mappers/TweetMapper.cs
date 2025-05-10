using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Response;
using Publisher.Models;
using Riok.Mapperly.Abstractions;
namespace Publisher.Mappers;

[Mapper]
public static partial class TweetMapper
{
    public static partial Tweet ToEntity(this CreateTweetRequestTo createTweetRequestTo);
    public static partial TweetResponseTo ToResponse(this Tweet tweet);
    public static partial IEnumerable<TweetResponseTo> ToResponse(this IEnumerable<Tweet> tweets);
}