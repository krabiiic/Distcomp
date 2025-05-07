using Publisher.Clients.Discussion.Dto.Request;
using Publisher.Clients.Discussion.Dto.Response;
using Refit;
namespace Publisher.Clients.Discussion;

public interface IDiscussionService
{
    [Get("/notices/{id}")]
    Task<DiscussionNoticeResponseTo> GetNotice(long id);

    [Get("/notices")]
    Task<IEnumerable<DiscussionNoticeResponseTo>> GetNotices();

    [Post("/notices")]
    Task<DiscussionNoticeResponseTo> CreateNotice([Body] DiscussionNoticeRequestTo createNoticeRequestTo);

    [Delete("/notices/{id}")]
    Task DeleteNotice(long id);

    [Put("/notices")]
    Task<DiscussionNoticeResponseTo> UpdateNotice([Body] DiscussionNoticeRequestTo noticeRequestTo);
}