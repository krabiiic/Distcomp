using Application.DTO.Request.Post;
using Application.DTO.Response.Post;
using AutoMapper;
using Domain.Entities;

namespace Application.DTO.Mapper;

public class GeneralProfile : Profile
{
    public GeneralProfile()
    {
        CreateMap<PostRequestToCreate, Post>();
        CreateMap<PostRequestToFullUpdate, Post>();
        CreateMap<Post, PostResponseToGetById>();
    }
}