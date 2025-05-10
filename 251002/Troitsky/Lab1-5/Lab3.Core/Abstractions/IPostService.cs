using Lab3.Core.Contracts;
using Lab3.Core.Models;

namespace Lab3.Core.Abstractions
{
    public interface IPostService
    {
        PostResponseTo? GetPost(ulong id);
        PostResponseTo CreatePost(Post post);
        bool DeletePost(ulong id);
        List<PostResponseTo> GetAllPosts();
        PostResponseTo? UpdatePost(Post post, ulong id);
    }
}
