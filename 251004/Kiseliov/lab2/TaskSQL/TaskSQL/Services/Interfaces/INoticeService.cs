using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;

namespace TaskSQL.Services.Interfaces;

public interface INoticeService
{
    Task<NoticeResponseTo> GetNoticeById(long id);
    Task<IEnumerable<NoticeResponseTo>> GetNotices();
    Task<NoticeResponseTo> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo);
    Task DeleteNotice(long id);
    Task<NoticeResponseTo> UpdateNotice(UpdateNoticeRequestTo modifiedNotice);
}