using lab2_jpa.DTO.Request.CreateTo;
using lab2_jpa.DTO.Request.UpdateTo;
using lab2_jpa.DTO.Response;

namespace lab2_jpa.Services.Interfaces
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
