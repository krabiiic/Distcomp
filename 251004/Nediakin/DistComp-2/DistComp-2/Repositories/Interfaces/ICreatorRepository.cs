using DistComp.DTO.RequestDTO;
using DistComp.DTO.ResponseDTO;
using DistComp.Models;

namespace DistComp.Repositories.Interfaces;

public interface ICreatorRepository : IRepository<Creator>
{
    public Task<bool> HasLogin(string creatorLogin);
    public Task<bool> HasUser(long creatorUserId);
}