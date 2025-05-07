using Discussion.Models;

namespace Discussion.Repositories.Interfaces;

public interface IPostRepository : ICassandraRepository<Post>
{
    
}