using AutoMapper;
using Lab1.Infrastructure.Entities;
using Lab1.Core.Models;
using System.Numerics;
using Lab1.Core.Contracts;
namespace Lab1.Infrastructure.MappingProfiles
{
    public class InfrastructureMappingProfile : Profile
    {
        // Определяет правила маппинга между сущностями базы данных
        public InfrastructureMappingProfile()
        {
            // src - из чего мапится
            // dest - куда
            // member - то, что мапится (здесь - member)
            CreateMap<User, UserEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
            CreateMap<News, NewsEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
            CreateMap<Tag, TagEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
//            CreateMap<Post, PostEntity>()
//                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
            // Преобразование из сущностей БД в DTO
            CreateMap<UserEntity, UserResponseTo>()
                .ForMember(dest => dest.firstname, opt => opt.MapFrom(src => src.FirstName))
                .ForMember(dest => dest.lastname, opt => opt.MapFrom(src => src.LastName))
                .ForMember(dest => dest.login, opt => opt.MapFrom(src => src.Login))
                .ForMember(dest => dest.password, opt => opt.MapFrom(src => src.Password))
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id));

            CreateMap<NewsEntity, NewsResponseTo>()
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.userId, opt => opt.MapFrom(src => src.UserId))
                .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.Content))
                .ForMember(dest => dest.title, opt => opt.MapFrom(src => src.Title))
                .ForMember(dest => dest.created, opt => opt.MapFrom(src => src.Created))
                .ForMember(dest => dest.modified, opt => opt.MapFrom(src => src.Modified));

            CreateMap<TagEntity, TagResponseTo>()
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.name, opt => opt.MapFrom(src => src.Name));
/*
            CreateMap<PostEntity, PostResponseTo>()
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.newsId, opt => opt.MapFrom(src => src.NewsId))
                .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.Content));
*/
        }
    }
}
