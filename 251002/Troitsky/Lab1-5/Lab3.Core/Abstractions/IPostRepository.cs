using Lab3.Core.Contracts;
using Lab3.Core.Models;

namespace Lab3.Core.Abstractions
{
    public interface IPostRepository
    {
        PostResponseTo? Get(ulong id);
        PostResponseTo Create(Post post);
        List<PostResponseTo> GetAll();
        bool Delete(ulong id);
        PostResponseTo? Update(Post post, ulong id);
    }
}
