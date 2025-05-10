using Publisher.Dto.Request.CreateTo;
using Publisher.Dto.Request.UpdateTo;
using Publisher.Dto.Response;
namespace Publisher.Services.Interfaces;

public interface ICreatorService
{
    Task<CreatorResponseTo> GetCreatorById(long id);
    Task<IEnumerable<CreatorResponseTo>> GetCreators();
    Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo);
    Task DeleteCreator(long id);
    Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator);
}