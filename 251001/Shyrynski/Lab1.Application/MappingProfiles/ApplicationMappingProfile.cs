using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
namespace Lab1.Application.MappingProfiles
{
    public class ApplicationMappingProfile : Profile
    {
        public ApplicationMappingProfile()
        {
            CreateMap<CreatorRequestTo, Creator>();
            CreateMap<CreatorUpdateRequest, Creator>()
                .ConstructUsing(src => Creator.Construct(src.Login, src.Password, src.FirstName, src.LastName));

            CreateMap<IssueRequestTo, Issue>();
            CreateMap<IssueUpdateRequest, Issue>()
                .ConstructUsing(src => Issue.Construct(src.CreatorId, src.Title, src.Content));

            CreateMap<StickerRequestTo, Sticker>();
            CreateMap<StickerUpdateRequest, Sticker>()
                .ConstructUsing(src => Sticker.Construct(src.Name));
        }
    }
}
