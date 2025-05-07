using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Models;

namespace DistComp_1.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<User, UserResponseDTO>();
        CreateMap<UserRequestDTO, User>();

        CreateMap<Message, MessageResponseDTO>();
        CreateMap<MessageRequestDTO, Message>();

        CreateMap<Mark, MarkResponseDTO>();
        CreateMap<MarkRequestDTO, Mark>();

        CreateMap<Article, ArticleResponseDTO>();
        CreateMap<ArticleRequestDTO, Article>();
    }
}