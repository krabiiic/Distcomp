using Microsoft.EntityFrameworkCore;
using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;
using TaskSQL.Exceptions;
using TaskSQL.Models;
using TaskSQL.Services.Interfaces;
using TaskSQL.Storage;
using NoticeMapper = TaskSQL.Mappers.NoticeMapper;

namespace TaskSQL.Services.Implementations;

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
        var notice = await context.Notices.FindAsync(modifiedNotice.id);
        if (notice == null) throw new EntityNotFoundException($"Notice with id = {modifiedNotice.id} doesn't exist.");

        context.Entry(notice).State = EntityState.Modified;

        notice.id = modifiedNotice.id;
        notice.Content = modifiedNotice.Content;
        notice.Tweetid = modifiedNotice.Tweetid;

        await context.SaveChangesAsync();
        return NoticeMapper.Map(notice);
    }
}