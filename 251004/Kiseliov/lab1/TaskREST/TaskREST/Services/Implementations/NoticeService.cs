using Microsoft.EntityFrameworkCore;
using TaskREST.Dto.Request.CreateTo;
using TaskREST.Dto.Request.UpdateTo;
using TaskREST.Dto.Response;
using TaskREST.Exceptions;
using TaskREST.Services.Interfaces;
using TaskREST.Storage;
using NoticeMapper = TaskREST.Mappers.NoticeMapper;

namespace TaskREST.Services.Implementations;

public sealed class NoticeService(AppDbContext context) : INoticeService
{
    public async Task<NoticeResponseTo> GetNoticeById(long id)
    {
        var notice = await context.Notices.FindAsync(id);
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {id} doesn't exist.");

        return NoticeMapper.Map(notice);
    }

    public async Task<IEnumerable<NoticeResponseTo>> GetNotices()
    {
        return NoticeMapper.Map(await context.Notices.ToListAsync());
    }

    public async Task<NoticeResponseTo> CreateNotice(CreateNoticeRequestTo createNoticeRequestTo)
    {
        var notice = NoticeMapper.Map(createNoticeRequestTo);
        await context.Notices.AddAsync(notice);
        await context.SaveChangesAsync();
        return NoticeMapper.Map(notice);
    }

    public async Task DeleteNotice(long id)
    {
        var notice = await context.Notices.FindAsync(id);
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {id} doesn't exist.");

        context.Notices.Remove(notice);
        await context.SaveChangesAsync();
    }

    public async Task<NoticeResponseTo> UpdateNotice(UpdateNoticeRequestTo modifiedNotice)
    {
        var notice = await context.Notices.FindAsync(modifiedNotice.Id);
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {modifiedNotice.Id} doesn't exist.");

        context.Entry(notice).State = EntityState.Modified;

        notice.Id = modifiedNotice.Id;
        notice.Content = modifiedNotice.Content;
        notice.TweetId = modifiedNotice.TweetId;

        await context.SaveChangesAsync();
        return NoticeMapper.Map(notice);
    }
}