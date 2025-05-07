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
using static System.Net.Mime.MediaTypeNames;

namespace Application.services
{
    public class TagService(AppDbContext appDbContext) : ITagService
    {
        private readonly AppDbContext _context = appDbContext;

        public async Task<TagResponseTo> CreateAsync(TagRequestTo request)
        {
            var entity = new Tag { Name = request.Name };
            var entry = await _context.Tags.AddAsync(entity);
            await _context.SaveChangesAsync();

            return new TagResponseTo
            {
                Id = entry.Entity.Id,
                Name = entry.Entity.Name
            };
        }

        public async Task<bool> DeleteAsync(long id)
        {
            bool isLinked = await _context.StoryTags.AnyAsync(st => st.TagId == id);
            if (isLinked) return false;

            var tag = await _context.Tags.FindAsync(id);
            if (tag == null) return false;

            _context.Tags.Remove(tag);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<IEnumerable<TagResponseTo>> GetAllAsync()
        {
            var entities = await _context.Tags.ToListAsync();
            return entities.Select(tag => new TagResponseTo
            {
                Id = tag.Id,
                Name = tag.Name
            });
        }

        public async Task<TagResponseTo?> GetByIdAsync(long id)
        {
            var entity = await _context.Tags.FindAsync(id);
            if (entity == null)
                return null;

            return new TagResponseTo
            {
                Id = entity.Id,
                Name = entity.Name
            };
        }

        public async Task<TagResponseTo?> UpdateAsync(long id, TagRequestTo request)
        {
            var entity = await _context.Tags.FindAsync(id);
            if (entity == null)
                return null;

            entity.Name = request.Name;
            _context.Tags.Update(entity);
            await _context.SaveChangesAsync();

            return new TagResponseTo
            {
                Id = entity.Id,
                Name = entity.Name
            };
        }
    }
}
