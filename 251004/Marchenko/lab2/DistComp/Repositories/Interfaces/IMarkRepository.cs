using DistComp.Models;

namespace DistComp.Repositories.Interfaces;

public interface IMarkRepository : IRepository<Mark>
{
    Task<IEnumerable<Mark>> GetByNamesAsync(IEnumerable<string> names);

}