using AutoMapper;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Models;

namespace Publisher.Infrastructure.Mapper;

public class MappingProfile : Profile
{
    public MappingProfile()
    {
        CreateMap<Creator, CreatorResponseDTO>();
        CreateMap<CreatorRequestDTO, Creator>();

        CreateMap<Mark, MarkResponseDTO>();
        CreateMap<MarkRequestDTO, Mark>();

        CreateMap<Issue, IssueResponseDTO>();
        CreateMap<IssueRequestDTO, Issue>();
    }
}