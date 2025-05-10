using System.Text;
using System.Text.Json;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.HttpClients.Interfaces;

namespace Publisher.HttpClients.Implementations;

public class DiscussionClient : IDiscussionClient
{
    private static readonly JsonSerializerOptions? JsonSerializerOptions = new()
    {
        PropertyNameCaseInsensitive = true,
    };

    private readonly IHttpClientFactory _factory;

    public DiscussionClient(IHttpClientFactory factory)
    {
        _factory = factory;
    }
    
    public async Task<IEnumerable<PostResponseDTO>?> GetPostsAsync()
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var uri = new Uri("posts", UriKind.Relative);
        var response = await httpClient.GetAsync(uri);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<IEnumerable<PostResponseDTO>>(responseJson, JsonSerializerOptions);
    }

    public async Task<PostResponseDTO?> GetPostByIdAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.GetAsync(new Uri($"posts/{id}", UriKind.Relative));
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<PostResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task<PostResponseDTO?> CreatePostAsync(PostRequestDTO post)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PostAsync("posts", content);
        response.EnsureSuccessStatusCode();
        
        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<PostResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task<PostResponseDTO?> UpdatePostAsync(PostRequestDTO post)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PutAsync("posts", content);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<PostResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task DeletePostAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.DeleteAsync($"posts/{id}");
        response.EnsureSuccessStatusCode();
    }
}