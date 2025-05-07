using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;

namespace Publisher.HttpClients.Interfaces;

public interface IDiscussionClient
{
    Task<IEnumerable<PostResponseDTO>?> GetPostsAsync();

    Task<PostResponseDTO?> GetPostByIdAsync(long id);

    Task<PostResponseDTO?> CreatePostAsync(PostRequestDTO post);

    Task<PostResponseDTO?> UpdatePostAsync(PostRequestDTO post);

    Task DeletePostAsync(long id);
}