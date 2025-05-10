using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
namespace Publisher.Services.Interfaces;

public interface INoticeService
{
    Task<NoticeResponseTo> GetNoticeById(long id);
    Task<IEnumerable<NoticeResponseTo>> GetNotices();
    Task<NoticeResponseTo> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo, string country);
    Task DeleteNotice(long id);
    Task<NoticeResponseTo> UpdateNotice(UpdateNoticeRequestTo modifiedNotice, string country);
}