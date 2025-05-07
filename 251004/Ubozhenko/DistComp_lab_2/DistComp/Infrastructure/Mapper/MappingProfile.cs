using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Models;
using LabelResponseDTO = DistComp.DTO.ResponseDTO.LabelResponseDTO;
using NewsRequestDTO = DistComp.DTO.ResponseDTO.NewsRequestDTO;

namespace DistComp.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Author, AuthorResponseDTO>();
        CreateMap<AuthorRequestDTO, Author>();

        CreateMap<Reaction, ReactionResponseDTO>();
        CreateMap<ReactionRequestDTO, Reaction>();

        CreateMap<Label, LabelResponseDTO>();
        CreateMap<DTO.RequestDTO.LabelResponseDTO, Label>();

        CreateMap<News, NewsRequestDTO>();
        CreateMap<DTO.RequestDTO.NewsRequestDTO, News>();
    }
}