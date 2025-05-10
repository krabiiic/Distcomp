using System.Text;
using System.Text.Json;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.HttpClients.Interfaces;
using Publisher.Repositories.Interfaces;
using Publisher.Services.Interfaces;

namespace Publisher.HttpClients.Implementations;

public class DiscussionClient : IDiscussionClient
{
    private static readonly JsonSerializerOptions? JsonSerializerOptions = new()
    {
        PropertyNameCaseInsensitive = true,
    };

    private readonly IHttpClientFactory _factory;
    private readonly IIssueRepository _issueRepository;

    public DiscussionClient(IHttpClientFactory factory, IIssueRepository issueRepository)
    {
        _factory = factory;
        _issueRepository = issueRepository;
    }
    
    public async Task<IEnumerable<NoteResponseDTO>?> GetNotesAsync()
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var uri = new Uri("notes", UriKind.Relative);
        var response = await httpClient.GetAsync(uri);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<IEnumerable<NoteResponseDTO>>(responseJson, JsonSerializerOptions);
    }

    public async Task<NoteResponseDTO?> GetNoteByIdAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.GetAsync(new Uri($"notes/{id}", UriKind.Relative));
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<NoteResponseDTO>(responseJson, JsonSerializerOptions);

    }

    public async Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO post)
    {
        if (!await _issueRepository.HasIssue(post.IssueId))
        {
            throw new ConflictException(ErrorCodes.IssueNotFound, ErrorMessages.IssueNotFoundMessage(post.IssueId));
        }
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PostAsync("notes", content);
        response.EnsureSuccessStatusCode();
        
        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<NoteResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task<NoteResponseDTO?> UpdateNoteAsync(NoteRequestDTO post)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var postJson = JsonSerializer.Serialize(post);
        var content = new StringContent(postJson, Encoding.UTF8, "application/json");

        var response = await httpClient.PutAsync("notes", content);
        response.EnsureSuccessStatusCode();

        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<NoteResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task DeleteNoteAsync(long id)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var response = await httpClient.DeleteAsync($"notes/{id}");
        response.EnsureSuccessStatusCode();
    }
}