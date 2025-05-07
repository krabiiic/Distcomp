using publisher.DTO.Request;
using publisher.DTO.Response;

namespace publisher.Services.Interfaces
{
    public interface ICreatorService
    {
        Task<CreatorResponseTo> GetCreatorById(long id);
        Task<IEnumerable<CreatorResponseTo>> GetCreators();
        Task<CreatorResponseTo> CreateCreator(CreateCreatorRequestTo createCreatorRequestTo);
        Task DeleteCreator(long id);
        Task<CreatorResponseTo> UpdateCreator(UpdateCreatorRequestTo modifiedCreator);
    }
}
