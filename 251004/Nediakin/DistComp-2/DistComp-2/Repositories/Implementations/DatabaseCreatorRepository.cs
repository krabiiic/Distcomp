using DistComp.Data;
using DistComp.Exceptions;
using DistComp.Models;
using DistComp.Repositories.Interfaces;

namespace DistComp.Repositories.Implementations;

public class DatabaseCreatorRepository : BaseDatabaseRepository<Creator>, ICreatorRepository
{
    public DatabaseCreatorRepository(AppDbContext context) : base(context)
    {
        // _dbSet.Add(new Creator()
        // {
        //     Login = "nedyasha@gmail.com",
        //     Password = "21050312",
        //     Firstname = "Александр",
        //     Lastname = "Недякин"
        // });
    }


    public virtual async Task<bool> HasLogin(string creatorLogin) => _dbSet.Any(t => t.Login == creatorLogin);
    public virtual async Task<bool> HasUser(long creatorUserId) => _dbSet.Any(t => t.Id == creatorUserId);
}   