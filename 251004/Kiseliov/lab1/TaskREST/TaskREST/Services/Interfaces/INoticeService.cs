using TaskREST.Dto.Request.CreateTo;
using TaskREST.Dto.Request.UpdateTo;
using TaskREST.Dto.Response;

namespace TaskREST.Services.Interfaces;

public interface INoticeService
{
    Task<NoticeResponseTo> GetNoticeById(long id);
    Task<IEnumerable<NoticeResponseTo>> GetNotices();
    Task<NoticeResponseTo> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo);
    Task DeleteNotice(long id);
    Task<NoticeResponseTo> UpdateNotice(UpdateNoticeRequestTo modifiedNotice);
}