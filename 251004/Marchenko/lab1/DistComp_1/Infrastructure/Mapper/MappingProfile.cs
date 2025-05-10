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

        CreateMap<Message, MessageResponseDTO>();
        CreateMap<MessageRequestDTO, Message>();

        CreateMap<Mark, MarkResponseDTO>();
        CreateMap<MarkRequestDTO, Mark>();

        CreateMap<Issue, IssueResponseDTO>();
        CreateMap<IssueRequestDTO, Issue>();
    }
}