using Riok.Mapperly.Abstractions;
using TaskREST.Dto.Request.CreateTo;
using TaskREST.Dto.Request.UpdateTo;
using TaskREST.Dto.Response;
using TaskREST.Models;

namespace TaskREST.Mappers;

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