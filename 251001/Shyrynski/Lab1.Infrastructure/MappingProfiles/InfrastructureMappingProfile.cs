using AutoMapper;
using Lab1.Infrastructure.Entities;
using Lab1.Core.Models;
using System.Numerics;
using Lab1.Core.Contracts;
namespace Lab1.Infrastructure.MappingProfiles
{
    public class InfrastructureMappingProfile : Profile
    {
        public InfrastructureMappingProfile()
        {
            CreateMap<Creator, CreatorEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
            CreateMap<Issue, IssueEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));
            CreateMap<Sticker, StickerEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0));

            CreateMap<CreatorEntity, CreatorResponseTo>()
                .ForMember(dest => dest.firstname, opt => opt.MapFrom(src => src.FirstName))
                .ForMember(dest => dest.lastname, opt => opt.MapFrom(src => src.LastName))
                .ForMember(dest => dest.login, opt => opt.MapFrom(src => src.Login))
                .ForMember(dest => dest.password, opt => opt.MapFrom(src => src.Password))
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id));

            CreateMap<IssueEntity, IssueResponseTo>()
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.creatorId, opt => opt.MapFrom(src => src.CreatorId))
                .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.Content))
                .ForMember(dest => dest.title, opt => opt.MapFrom(src => src.Title))
                .ForMember(dest => dest.created, opt => opt.MapFrom(src => src.Created))
                .ForMember(dest => dest.modified, opt => opt.MapFrom(src => src.Modified));

            CreateMap<StickerEntity, StickerResponseTo>()
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.name, opt => opt.MapFrom(src => src.Name));
        }
    }
}
