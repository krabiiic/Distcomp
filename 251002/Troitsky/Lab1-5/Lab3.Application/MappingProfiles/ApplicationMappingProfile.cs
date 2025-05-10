using Lab3.Application.Contracts;
using Lab3.Core.Models;
using AutoMapper;

namespace Lab3.Application.MappingProfiles
{
    public class ApplicationMappingProfile : Profile
    {
        public ApplicationMappingProfile()
        {
            CreateMap<PostRequestTo, Post>()
                .ConstructUsing(src => Post.Construct(src.NewsId, src.Content)); 
            CreateMap<PostUpdateRequest, Post>()
                .ConstructUsing(src => Post.Construct(src.NewsId, src.Content));
        }
    }
}
