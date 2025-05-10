using Cassandra.Data.Linq;
using Discussion.Dto.Request;
using Discussion.Dto.Response;
using Discussion.Exceptions;
using Discussion.Mappers;
using Discussion.Models;
using Discussion.Services.Interfaces;
using Discussion.Storage;
namespace Discussion.Services.Implementations;

public sealed class NoticeService(CassandraDbContext context, ILogger<NoticeService> logger) : INoticeService
{
    public async Task<NoticeResponseTo> GetNoticeById(long id)
    {
        Notice? notice = await context.Notices.FirstOrDefault(p => p.Id == id).ExecuteAsync();
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {id} doesn't exist.");

        logger.LogInformation("Notice with id = {Id} was found", id);
        return notice.ToResponse();
    }

    public async Task<IEnumerable<NoticeResponseTo>> GetNotices()
    {
        logger.LogInformation("Getting all notices");
        return (await context.Notices.ExecuteAsync()).ToResponse();
    }

    public async Task<NoticeResponseTo> CreateNotice(NoticeRequestTo noticeRequestTo)
    {
        Notice notice = noticeRequestTo.ToEntity();
        await context.Notices.Insert(notice).ExecuteAsync();
        logger.LogInformation("Notice with id = {Id} was created", notice.Id);
        return notice.ToResponse();
    }

    public async Task DeleteNotice(long id)
    {
        Notice? notice = await context.Notices.FirstOrDefault(p => p.Id == id).ExecuteAsync();
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {id} doesn't exist.");
        
        await context.Notices.Where(p => p.Country == notice.Country && p.TweetId == notice.TweetId && p.Id == notice.Id)
            .Delete()
            .ExecuteAsync();
        logger.LogInformation("Notice with id = {Id} was deleted", id);
    }

    public async Task<NoticeResponseTo> UpdateNotice(NoticeRequestTo modifiedNotice)
    {
        Notice? notice = await context.Notices.FirstOrDefault(p => p.Id == modifiedNotice.Id).ExecuteAsync();
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {modifiedNotice.Id} doesn't exist.");
        
        await context.Notices.Where(p => p.Country == notice.Country && p.TweetId == notice.TweetId && p.Id == notice.Id)
            .Select(p => new Notice
            {
                Content = modifiedNotice.Content
            })
            .Update()
            .ExecuteAsync();
        
        return modifiedNotice.ToEntity().ToResponse();
    }
}
