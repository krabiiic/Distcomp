using Core.DTO;
using AutoMapper;
using Core.Entities;

namespace Core.DTO;

public class GeneralProfile : Profile
{
    public GeneralProfile()
    {
        CreateMap<MessageRequestToCreate, Message>();
        CreateMap<MessageRequestToFullUpdate, Message>();
        CreateMap<Message, MessageResponseToGetById>();
    }
}