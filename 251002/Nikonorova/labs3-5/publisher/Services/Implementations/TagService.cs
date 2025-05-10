using Microsoft.EntityFrameworkCore;
using publisher.DTO.Request;
using publisher.DTO.Response;
using publisher.Models;
using publisher.Storage;
using publisher.Services.Interfaces;
using publisher.Exceptions;
using publisher.Mappers;
using System;

namespace publisher.Services.Implementations
{
    public sealed class TagService(AppDbContext context) : ITagService
    {
        public async Task<TagResponseTo> GetTagById(long id)
        {
            Tag? tag = await context.Tags.FindAsync(id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {id} doesn't exist.");

            return tag.ToResponse();
        }

        public async Task<IEnumerable<TagResponseTo>> GetTags()
        {
            return (await context.Tags.ToListAsync()).ToResponse();
        }

        public async Task<TagResponseTo> CreateTag(CreateTagRequestTo createTagRequestTo)
        {
            Tag tag = createTagRequestTo.ToEntity();
            await context.Tags.AddAsync(tag);
            await context.SaveChangesAsync();
            return tag.ToResponse();
        }

        public async Task DeleteTag(long id)
        {
            Tag? tag = await context.Tags.FindAsync(id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {id} doesn't exist.");

            context.Tags.Remove(tag);
            await context.SaveChangesAsync();
        }

        public async Task<TagResponseTo> UpdateTag(UpdateTagRequestTo modifiedTag)
        {
            Tag? tag = await context.Tags.FindAsync(modifiedTag.Id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {modifiedTag.Id} doesn't exist.");

            context.Entry(tag).State = EntityState.Modified;

            tag.Id = modifiedTag.Id;
            tag.Name = modifiedTag.Name;

            await context.SaveChangesAsync();
            return tag.ToResponse();
        }
    }
}
