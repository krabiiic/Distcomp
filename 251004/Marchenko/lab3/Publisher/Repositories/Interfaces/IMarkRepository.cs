using Publisher.Models;

namespace Publisher.Repositories.Interfaces;

public interface IMarkRepository : IRepository<Mark>
{
    Task<IEnumerable<Mark>> GetByNamesAsync(IEnumerable<string> names);

}