using Lab3.Application.Contracts;
using Lab3.Core.Models;
using AutoMapper;

namespace Lab3.Application.MappingProfiles
{
    public class ApplicationMappingProfile : Profile
    {
        public ApplicationMappingProfile()
        {
            CreateMap<MessageRequestTo, Message>();
            CreateMap<MessageUpdateRequest, Message>()
                .ConstructUsing(src => Message.Construct(src.IssueId, src.Content));
        }
    }
}
