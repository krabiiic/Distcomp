using AutoMapper;
using DistComp.Data;
using DistComp.Models;
using Microsoft.EntityFrameworkCore;
using Redis.OM;
using Redis.OM.Searching;

namespace DistComp.Services.Implementation {
    public class TopicService : ICommonService<long, Topic, TopicInDto, TopicOutDto> {

        private DCContext context;
        private RedisConnectionProvider provider;
        private IRedisCollection<TopicOutDto> topics;
        private IMapper mapper;

        public TopicService(DCContext context, RedisConnectionProvider provider, IMapper mapper) {
            this.context = context;
            this.mapper = mapper;
            this.provider = provider;

            topics = provider.RedisCollection<TopicOutDto>();
        }

        public IEnumerable<TopicOutDto> GetAll() {
            var cached = topics.ToList();
            if (cached.Count > 0) {
                return cached;
            }

            return context.Topics.ToList().Select(e => mapper.Map<TopicOutDto>(e));
        }

        public TopicOutDto? Get(long id) {
            var cached = topics.FindById(id.ToString());
            if (cached is not null) {
                return cached;
            }

            var topic = context.Topics.Find(id);
            if (topic == null) {
                return null;
            } else {
                return mapper.Map<TopicOutDto>(topic);
            }
        }

        public TopicOutDto Create(TopicInDto data) {
            var newTopic = mapper.Map<Topic>(data);
            context.Topics.Add(newTopic);

            foreach (var tagString in newTopic.TagStrings) {
                var tag = context.Tags.FirstOrDefault(t => t.Name == tagString);
                if (tag == null) {
                    newTopic.Tags.Add(new Tag { Name = tagString });
                } else {
                    tag.Topics.Add(newTopic);
                }
            }
            context.SaveChanges();

            var topicOut = mapper.Map<TopicOutDto>(newTopic);

            topics.Insert(topicOut);

            return topicOut;
        }

        public TopicOutDto Update(Topic data) {
            var topic = context.Topics.Find(data.Id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            topic.UserId = data.UserId;
            topic.Title = data.Title;
            topic.Content = data.Content;
            context.SaveChanges();

            var topicOut = mapper.Map<TopicOutDto>(data);

            topics.Update(topicOut);

            return topicOut;
        }

        public void Delete(long id) {
            var topic = context.Topics
                .Include(topic => topic.Tags)
                .FirstOrDefault(t => t.Id == id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            context.Topics.Remove(topic);
            context.SaveChanges();

            foreach (var tag in topic.Tags) {
                if (!context.Tags.Where(t => t.Id == tag.Id).SelectMany(t => t.Topics).Any()) {
                    context.Tags.Remove(tag);
                }
            }
            context.SaveChanges();

            provider.Connection.Unlink($"{nameof(Topic)}:{id}");
        }
    }
}
