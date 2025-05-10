using AutoMapper;
using DistComp_1.DTO.RequestDTO;
using DistComp_1.DTO.ResponseDTO;
using DistComp_1.Models;

namespace DistComp_1.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Creator, CreatorResponseDTO>();
        CreateMap<CreatorRequestDTO, Creator>();

        CreateMap<Note, NoteResponseDTO>();
        CreateMap<NoteRequestDTO, Note>();

        CreateMap<Sticker, StickerResponseDTO>();
        CreateMap<StickerRequestDTO, Sticker>();

        CreateMap<Issue, IssueResponseDTO>();
        CreateMap<IssueRequestDTO, Issue>();
    }
}