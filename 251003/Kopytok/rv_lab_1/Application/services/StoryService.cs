using Application.abstractions;
using Core;
using Database;
using DTO.requests;
using DTO.responces;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Application.services
{
    public class StoryService(AppDbContext appDbContext, ITagService tagService) : IStoryService
    {
        private AppDbContext _context = appDbContext;
        private ITagService _tagService = tagService;
        public async Task<StoryResponseTo?> CreateAsync(StoryRequestTo request)
        {
            var editorExists = await _context.Editors.AnyAsync(e => e.Id == request.EditorId);
            if (!editorExists)
                return null;

            var storyExists = await _context.Stories.AnyAsync(s => s.Title == request.Title);
            if (storyExists)
                return null;

            var entity = new Story
            {
                Content = request.Content,
                Title = request.Title,
                EditorId = request.EditorId,
                Created = DateTime.UtcNow,
                Modified = DateTime.UtcNow
            };

            var entry = await _context.Stories.AddAsync(entity);
            await _context.SaveChangesAsync();

            if (request.Tags.Count != 0)
            {
                foreach (var tagName in request.Tags)
                {
                    var tag = await _context.Tags.FirstOrDefaultAsync(t => t.Name == tagName);
                    if (tag == null)
                    {
                        var tagRes = await _tagService.CreateAsync(new TagRequestTo { Name = tagName });
                        tag = await _context.Tags.FirstOrDefaultAsync(t => t.Name == tagName);
                    }

                    if (tag != null)
                    {
                        _context.StoryTags.Add(new StoryTag { StoryId = entry.Entity.Id, TagId = tag.Id });
                        await _context.SaveChangesAsync();
                    }
                }
            }

            return new StoryResponseTo
            {
                Id = entry.Entity.Id,
                Content = entry.Entity.Content,
                Title = entry.Entity.Title,
                EditorId = entry.Entity.EditorId,
                Created = entry.Entity.Created,
                Modified = entry.Entity.Modified
            };
        }


        public async Task<bool> DeleteAsync(long id)
        {
            var entity = await _context.Stories.FirstOrDefaultAsync(s => s.Id == id);
            if (entity == null)
                return false;

            var storyTags = await _context.StoryTags
                .Where(st => st.StoryId == id)
                .ToListAsync();

            _context.StoryTags.RemoveRange(storyTags);
            await _context.SaveChangesAsync();

            foreach (var storyTag in storyTags)
            {
                await _tagService.DeleteAsync(storyTag.TagId);
            }

            _context.Stories.Remove(entity);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<IEnumerable<StoryResponseTo>> GetAllAsync()
        {
            var entities = await _context.Stories.ToListAsync();
            return entities.Select(e => new StoryResponseTo
            {
                Id = e.Id,
                Content = e.Content,
                Title = e.Title,
                EditorId = e.EditorId,
                Created = e.Created,
                Modified = e.Modified
            });
        }

        public async Task<StoryResponseTo?> GetByIdAsync(long id)
        {
            var entity = await _context.Stories.FindAsync(id);
            if (entity == null)
                return null;

            return new StoryResponseTo
            {
                Id = entity.Id,
                Content = entity.Content,
                Title = entity.Title,
                EditorId = entity.EditorId,
                Created = entity.Created,
                Modified = entity.Modified
            };
        }

        public async Task<StoryResponseTo?> UpdateAsync(long id, StoryRequestTo request)
        {
            var entity = await _context.Stories.FindAsync(id);
            if (entity == null)
                return null;

            entity.Content = request.Content;
            entity.Title = request.Title;
            entity.EditorId = request.EditorId;
            entity.Modified = DateTime.UtcNow;

            _context.Stories.Update(entity);
            await _context.SaveChangesAsync();

            return new StoryResponseTo
            {
                Id = entity.Id,
                Content = entity.Content,
                Title = entity.Title,
                EditorId = entity.EditorId,
                Created = entity.Created,
                Modified = entity.Modified
            };
        }
    }
}
