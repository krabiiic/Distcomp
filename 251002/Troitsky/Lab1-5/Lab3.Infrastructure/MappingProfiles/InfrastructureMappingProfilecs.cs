using AutoMapper;
using Lab3.Infrastructure.Entities;
using Lab3.Core.Models;
using System.Numerics;
using Lab3.Core.Contracts;

namespace Lab3.Infrastructure.MappingProfiles
{
    public class InfrastructureMappingProfile : Profile
    {
        public InfrastructureMappingProfile()
        {
            // src - из чего мапится
            // dest - куда
            // member - то, что мапится (здесь - member)
            CreateMap<Post, PostEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0)); 

            CreateMap<PostEntity, PostResponseTo>()
                .ForMember(dest => dest.newsId, opt => opt.MapFrom(src => src.NewsId))
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.Content));
        }
    }
}
