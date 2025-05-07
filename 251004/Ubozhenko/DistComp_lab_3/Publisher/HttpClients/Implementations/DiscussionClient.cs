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
    
    public async Task<IEnumerable<ReactionResponseDTO>?> GetReactionsAsync()
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var uri = new Uri("reactions", UriKind.Relative);
        var response = await httpClient.GetAsync(uri);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<IEnumerable<ReactionResponseDTO>>(responseJson, JsonSerializerOptions);
    }

    public async Task<ReactionResponseDTO?> GetReactionByIdAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.GetAsync(new Uri($"reactions/{id}", UriKind.Relative));
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<ReactionResponseDTO>(responseJson, JsonSerializerOptions);

    }

    public async Task<ReactionResponseDTO?> CreateReactionAsync(ReactionRequestDTO post)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PostAsync("reactions", content);
        response.EnsureSuccessStatusCode();
        
        var responseJson = await response.Content.ReadAsStringAsync();
        var result = JsonSerializer.Deserialize<ReactionResponseDTO>(responseJson, JsonSerializerOptions);
        return result;
    }

    public async Task<ReactionResponseDTO?> UpdateReactionAsync(ReactionRequestDTO post)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PutAsync("reactions", content);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<ReactionResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task DeleteReactionAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.DeleteAsync($"reactions/{id}");
        response.EnsureSuccessStatusCode();
    }
}