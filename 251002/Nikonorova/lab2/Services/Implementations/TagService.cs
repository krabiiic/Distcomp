using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.EntityFrameworkCore;
using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;
using lab2_jpa.Exceptions;
using lab2_jpa.Mappers;
using lab2_jpa.Services.Interfaces;
using lab2_jpa.Models;
using System;

namespace lab2_jpa.Services.Implementations
{
    public sealed class TagService(AppDbContext context) : ITagService
    {
        public async Task<TagResponseTo> GetTagById(long id)
        {
            var tag = await context.Tags.FindAsync(id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {id} doesn't exist.");

            return TagMapper.Map(tag);
        }

        public async Task<IEnumerable<TagResponseTo>> GetTags()
        {
            return TagMapper.Map(await context.Tags.ToListAsync());
        }

        public async Task<TagResponseTo> CreateTag(CreateTagRequestTo createTagRequestTo)
        {
            var tag = TagMapper.Map(createTagRequestTo);
            await context.Tags.AddAsync(tag);
            await context.SaveChangesAsync();
            return TagMapper.Map(tag);
        }

        public async Task DeleteTag(long id)
        {
            var tag = await context.Tags.FindAsync(id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {id} doesn't exist.");

            context.Tags.Remove(tag);
            await context.SaveChangesAsync();
        }

        public async Task<TagResponseTo> UpdateTag(UpdateTagRequestTo modifiedTag)
        {
            var tag = await context.Tags.FindAsync(modifiedTag.id);
            if (tag == null) throw new EntityNotFoundException($"Tag with id = {modifiedTag.id} doesn't exist.");

            context.Entry(tag).State = EntityState.Modified;

            tag.id = modifiedTag.id;
            tag.Name = modifiedTag.Name;

            await context.SaveChangesAsync();
            return TagMapper.Map(tag);
        }
    }
}
