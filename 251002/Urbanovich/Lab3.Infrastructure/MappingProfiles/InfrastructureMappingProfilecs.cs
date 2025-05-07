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
            CreateMap<Message, MessageEntity>()
                .ForMember(dest => dest.Id, opt => opt.Condition((src, dest, member) => dest.Id == 0)); 

            CreateMap<MessageEntity, MessageResponseTo>()
                .ForMember(dest => dest.issueId, opt => opt.MapFrom(src => src.IssueId))
                .ForMember(dest => dest.id, opt => opt.MapFrom(src => src.Id))
                .ForMember(dest => dest.content, opt => opt.MapFrom(src => src.Content));
        }
    }
}
