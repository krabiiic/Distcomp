using Riok.Mapperly.Abstractions;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Models;

namespace TaskSQL.Mappers;

[Mapper]
public static partial class NoticeMapper
{
    public static partial Notice Map(UpdateNoticeRequestTo updateNoticeRequestTo);
    public static partial Notice Map(CreateNoticeRequestTo createNoticeRequestTo);
    public static partial NoticeResponseTo Map(Notice notice);
    public static partial IEnumerable<NoticeResponseTo> Map(IEnumerable<Notice> notices);

    public static partial IEnumerable<Notice> Map(
        IEnumerable<UpdateNoticeRequestTo> noticeRequestTos);
}