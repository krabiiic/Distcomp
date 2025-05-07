using AutoMapper;
using DistComp.Data;
using DistComp.Models;
using Microsoft.EntityFrameworkCore;
using Redis.OM;
using Redis.OM.Searching;

namespace DistComp.Services.Implementation {
    public class TagService : ICommonService<long, Tag, TagInDto, TagOutDto> {
        private DCContext context;
        private RedisConnectionProvider provider;
        private IRedisCollection<TagOutDto> tags;
        private IMapper mapper;

        public TagService(DCContext context, RedisConnectionProvider provider, IMapper mapper) {
            this.provider = provider;
            this.context = context;
            this.mapper = mapper;

            tags = provider.RedisCollection<TagOutDto>();
        }

        public IEnumerable<TagOutDto> GetAll() {
            var cached = tags.ToList();
            if (cached.Count > 0) {
                return cached;
            }

            return context.Tags.ToList().Select(e => mapper.Map<TagOutDto>(e));
        }

        public TagOutDto? Get(long id) {
            var cached = tags.FindById(id.ToString());
            if (cached is not null) {
                return cached;
            }

            var tag = context.Tags.Find(id);
            if (tag == null) {
                return null;
            } else {
                return mapper.Map<TagOutDto>(tag);
            }
        }


        public TagOutDto Create(TagInDto data) {
            var newTag = mapper.Map<Tag>(data);
            context.Tags.Add(newTag);
            context.SaveChanges();

            var tagOut = mapper.Map<TagOutDto>(newTag);

            tags.Insert(tagOut);

            return tagOut;
        }

        public TagOutDto Update(Tag data) {
            var tag = context.Tags.Find(data.Id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            tag.Name = data.Name;
            context.SaveChanges();

            var tagOut = mapper.Map<TagOutDto>(data);

            tags.Update(tagOut);

            return tagOut;
        }

        public void Delete(long id) {
            var tag = context.Tags.Include(t => t.Topics).FirstOrDefault(t => t.Id == id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            if (tag.Topics.Count() > 0) {
                throw new InvalidOperationException("Cannot delete tag using in one or more topics");
            }

            context.Tags.Remove(tag);
            context.SaveChanges();

            provider.Connection.Unlink($"{nameof(Tag)}:{id}");
        }
    }
}
