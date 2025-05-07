using TaskSQL.Dto.Request.CreateTo;
using TaskSQL.Dto.Request.UpdateTo;
using TaskSQL.Dto.Response;

namespace TaskSQL.Services.Interfaces;

public interface ICreatorService
{
    Task<CreatorResponseTo> GetCreatorById(long id);
    Task<IEnumerable<CreatorResponseTo>> GetCreators();
    Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo);
    Task DeleteCreator(long id);
    Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator);
}