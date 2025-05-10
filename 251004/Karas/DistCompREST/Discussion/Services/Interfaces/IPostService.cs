using Discussion.DTO.Request;
using Discussion.DTO.Response;

namespace Discussion.Services.Interfaces;

public interface IPostService
{
    Task<IEnumerable<PostResponseDTO>> GetPostsAsync();

    Task<PostResponseDTO> GetPostByIdAsync(long id);

    Task<PostResponseDTO> CreatePostAsync(PostRequestDTO post);

    Task<PostResponseDTO> UpdatePostAsync(PostRequestDTO post);

    Task DeletePostAsync(long id);
}