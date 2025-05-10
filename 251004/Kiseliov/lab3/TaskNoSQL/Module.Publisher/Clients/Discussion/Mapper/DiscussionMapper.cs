using Publisher.Clients.Discussion.Dto.Request;
using Publisher.Clients.Discussion.Dto.Response;
using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Riok.Mapperly.Abstractions;
namespace Publisher.Clients.Discussion.Mapper;

[Mapper]
public static partial class DiscussionMapper
{
    public static DiscussionNoticeRequestTo ToDiscussionRequest(this CreateNoticeRequestTo requestTo, long id, string country) =>
        new(id, requestTo.TweetId, requestTo.Content, country);
    public static DiscussionNoticeRequestTo ToDiscussionRequest(this UpdateNoticeRequestTo requestTo, string country) =>
        new(requestTo.Id, requestTo.TweetId, requestTo.Content, country);

    public static partial NoticeResponseTo ToResponse(this DiscussionNoticeResponseTo responseTo);

    public static partial IEnumerable<NoticeResponseTo> ToResponse(this IEnumerable<DiscussionNoticeResponseTo> responseTo);
}
