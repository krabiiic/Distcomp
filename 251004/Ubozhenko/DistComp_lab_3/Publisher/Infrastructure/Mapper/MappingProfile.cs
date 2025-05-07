using AutoMapper;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Models;

namespace Publisher.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Author, AuthorResponseDTO>();
        CreateMap<AuthorRequestDTO, Author>();

        CreateMap<Label, LabelResponseDTO>();
        CreateMap<LabelRequestDTO, Label>();

        CreateMap<News, NewsResponseDTO>();
        CreateMap<NewsRequestDTO, News>();
    }
}