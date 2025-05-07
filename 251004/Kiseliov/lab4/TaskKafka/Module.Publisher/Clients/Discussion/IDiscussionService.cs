using Publisher.Clients.Discussion.Dto.Request;
using Publisher.Clients.Discussion.Dto.Response;
namespace Publisher.Clients.Discussion;

public interface IDiscussionService
{
    Task<DiscussionNoticeResponseTo> GetNotice(long id);
    
    Task<IEnumerable<DiscussionNoticeResponseTo>> GetNotices();
    
    Task<DiscussionNoticeResponseTo> CreateNotice(DiscussionNoticeRequestTo createNoticeRequestTo);
    
    Task DeleteNotice(long id);
    
    Task<DiscussionNoticeResponseTo> UpdateNotice(DiscussionNoticeRequestTo noticeRequestTo);
}