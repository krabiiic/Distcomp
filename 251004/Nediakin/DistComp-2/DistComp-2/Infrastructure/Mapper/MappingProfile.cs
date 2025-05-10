using AutoMapper;
using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Models;

namespace DistComp.Infrastructure.Mapper;

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