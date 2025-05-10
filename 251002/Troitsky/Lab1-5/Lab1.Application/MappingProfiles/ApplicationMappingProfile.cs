using Lab1.Application.Contracts;
using Lab1.Core.Models;
using AutoMapper;
namespace Lab1.Application.MappingProfiles
{
    // AutoMapper - преобразует объекты из DTO в модель домена
    public class ApplicationMappingProfile : Profile
    {
        public ApplicationMappingProfile()
        {
            CreateMap<UserRequestTo, User>();
            CreateMap<UserUpdateRequest, User>()
                .ConstructUsing(src => User.Construct(src.Login, src.Password, src.FirstName, src.LastName));

            CreateMap<NewsRequestTo, News>();
            CreateMap<NewsUpdateRequest, News>()
                .ConstructUsing(src => News.Construct(src.UserId, src.Title, src.Content));

            CreateMap<TagRequestTo, Tag>();
            CreateMap<TagUpdateRequest, Tag>()
                .ConstructUsing(src => Tag.Construct(src.Name));

//           CreateMap<PostRequestTo, Post>();
//            CreateMap<PostUpdateRequest, Post>()
//                .ConstructUsing(src => Post.Construct(src.NewsId, src.Content));
        }
    }
}
