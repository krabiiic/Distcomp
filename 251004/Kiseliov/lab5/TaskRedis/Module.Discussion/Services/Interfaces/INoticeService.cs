using Discussion.Dto.Request;
using Discussion.Dto.Response;
namespace Discussion.Services.Interfaces;

public interface INoticeService
{
    Task<NoticeResponseTo> GetNoticeById(long id);
    Task<IEnumerable<NoticeResponseTo>> GetNotices();
    Task<NoticeResponseTo> CreateNotice(NoticeRequestTo noticeRequestTo);
    Task DeleteNotice(long id);
    Task<NoticeResponseTo> UpdateNotice(NoticeRequestTo modifiedNotice);
}