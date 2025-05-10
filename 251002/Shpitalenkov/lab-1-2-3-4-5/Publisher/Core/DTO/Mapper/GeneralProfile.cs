using Core.DTO;
using AutoMapper;
using Core.Entities;

namespace Core.DTO.Mapper;

public class GeneralProfile : Profile
{
    public GeneralProfile()
    {
        CreateMap<CreatorRequestToCreate, Creator>();
        CreateMap<CreatorRequestToFullUpdate, Creator>();
        CreateMap<Creator, CreatorResponseToGetById>();
        
        CreateMap<ArticleRequestToCreate, Article>();
        CreateMap<ArticleRequestToFullUpdate, Article>();
        CreateMap<Article, ArticleResponseToGetById>();
        
        CreateMap<TagRequestToCreate, Tag>();
        CreateMap<TagRequestToFullUpdate, Tag>();
        CreateMap<Tag, TagResponseToGetById>();

        CreateMap<TagRequestToCreateTagsIfDontExist, IEnumerable<Tag>?>();
    }
}