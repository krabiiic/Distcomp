using Discussion.Models;

namespace Discussion.Repositories.Interfaces;

public interface INoteRepository : ICassandraRepository<Note>
{
    
}