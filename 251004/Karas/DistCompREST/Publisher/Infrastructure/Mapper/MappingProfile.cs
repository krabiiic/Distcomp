using AutoMapper;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Models;

namespace Publisher.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Editor, EditorResponseDTO>();
        CreateMap<EditorRequestDTO, Editor>();

        CreateMap<Mark, MarkResponseDTO>();
        CreateMap<MarkRequestDTO, Mark>();

        CreateMap<Article, ArticleResponseDTO>();
        CreateMap<ArticleRequestDTO, Article>();
    }
}