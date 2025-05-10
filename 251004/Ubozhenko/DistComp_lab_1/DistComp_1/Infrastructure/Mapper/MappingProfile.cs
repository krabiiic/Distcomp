using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Models;

namespace DistComp_1.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Author, AuthorResponseDTO>();
        CreateMap<AuthorRequestDTO, Author>();

        CreateMap<Reaction, ReactionResponseDTO>();
        CreateMap<ReactionRequestDTO, Reaction>();

        CreateMap<Label, LabelResponseDTO>();
        CreateMap<LabelRequestDTO, Label>();

        CreateMap<News, NewsResponseDTO>();
        CreateMap<NewsRequestDTO, News>();
    }
}