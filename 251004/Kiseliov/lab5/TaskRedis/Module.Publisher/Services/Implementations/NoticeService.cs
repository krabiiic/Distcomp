using Microsoft.EntityFrameworkCore;
using Publisher.Clients.Discussion;
using Publisher.Clients.Discussion.Dto.Response;
using Publisher.Clients.Discussion.Mapper;
using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
using Publisher.Exceptions;
using Publisher.Services.Interfaces;
using Publisher.Storage;
namespace Publisher.Services.Implementations;

public sealed class NoticeService(IDiscussionService discussionService, AppDbContext dbContext) : INoticeService
{
    private readonly Random _random = new();

    public async Task<NoticeResponseTo> GetNoticeById(long id) =>
        (await discussionService.GetNotice(id)).ToResponse();

    public async Task<IEnumerable<NoticeResponseTo>> GetNotices() =>
        (await discussionService.GetNotices()).ToResponse();

    public async Task<NoticeResponseTo> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo, string country)
    {
        var isTweetExist = await dbContext.Tweets.AnyAsync(t => t.Id == createNoticeRequestTo.TweetId);
        if (!isTweetExist)
            throw new EntityNotFoundException("Tweet not found");

        DiscussionNoticeResponseTo discussionNoticeResponseTo = (await discussionService.CreateNotice(createNoticeRequestTo.ToDiscussionRequest(CreateId(), country)));
        return discussionNoticeResponseTo.ToResponse();
    }

    public async Task DeleteNotice(long id)
    {
        await discussionService.DeleteNotice(id);
    }

    public async Task<NoticeResponseTo> UpdateNotice(UpdateNoticeRequestTo modifiedNotice, string country)
    {
        var isTweetExist = await dbContext.Tweets.AnyAsync(t => t.Id == modifiedNotice.TweetId);
        if (!isTweetExist)
            throw new EntityNotFoundException("Tweet not found");

        return (await discussionService.UpdateNotice(modifiedNotice.ToDiscussionRequest(country))).ToResponse();
    }

    private long CreateId() => (DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() << 24 | (uint)_random.Next(0, 1 << 24)) & long.MaxValue;
}
