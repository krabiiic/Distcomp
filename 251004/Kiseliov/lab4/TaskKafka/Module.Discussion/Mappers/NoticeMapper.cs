using Discussion.Dto.Request;
using Discussion.Dto.Response;
using Discussion.Models;
using Riok.Mapperly.Abstractions;
namespace Discussion.Mappers;

[Mapper]
public static partial class NoticeMapper
{
    public static partial Notice ToEntity(this NoticeRequestTo noticeRequestTo);
    public static partial NoticeResponseTo ToResponse(this Notice notice);
    public static partial IEnumerable<NoticeResponseTo> ToResponse(this IEnumerable<Notice> notices);

    public static Notice UpdateEntity(this Notice notice, NoticeRequestTo updateNoticeRequestTo) => new()
    {
        Id = notice.Id,
        TweetId = updateNoticeRequestTo.TweetId,
        Content = updateNoticeRequestTo.Content,
        Country = notice.Country
    };
}
