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

        CreateMap<Message, MessageResponseDTO>();
        CreateMap<MessageRequestDTO, Message>();

        CreateMap<Mark, MarkResponseDTO>();
        CreateMap<MarkRequestDTO, Mark>();

        CreateMap<Issue, IssueResponseDTO>();
        CreateMap<IssueRequestDTO, Issue>();
    }
}