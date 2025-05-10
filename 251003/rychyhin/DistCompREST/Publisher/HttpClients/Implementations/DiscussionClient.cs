using System.Text;
using System.Text.Json;
using Publisher.DTO.RequestDTO;
using Publisher.DTO.ResponseDTO;
using Publisher.Exceptions;
using Publisher.HttpClients.Interfaces;
using Publisher.Repositories.Implementations;
using Publisher.Repositories.Interfaces;

namespace Publisher.HttpClients.Implementations;

public class DiscussionClient : IDiscussionClient
{
    private static readonly JsonSerializerOptions? JsonSerializerOptions = new()
    {
        PropertyNameCaseInsensitive = true,
    };

    private readonly IHttpClientFactory _factory;
    private readonly INewsRepository _newsRepository; 
 

    public DiscussionClient(IHttpClientFactory factory, INewsRepository newsRepository )
    {
        _factory = factory;
        _newsRepository = newsRepository;
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

    public async Task<NoteResponseDTO?> CreateNoteAsync(NoteRequestDTO note)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        
        var noteJson = JsonSerializer.Serialize(note);
        var content = new StringContent(noteJson, Encoding.UTF8, "application/json");
        
        var newsExists = await _newsRepository.GetByIdAsync(note.NewsId) != null;
        if (!newsExists)
        {
            throw new NotFoundException(404, $"News with ID {note.NewsId} not found.");
        }

        var response = await httpClient.PostAsync("notes", content);
        response.EnsureSuccessStatusCode();
        
        var responseJson = await response.Content.ReadAsStringAsync();
        return JsonSerializer.Deserialize<NoteResponseDTO>(responseJson, JsonSerializerOptions);
    }

    public async Task<NoteResponseDTO?> UpdateNoteAsync(long id, NoteRequestDTO note)
    {
        var httpClient = _factory.CreateClient(nameof(DiscussionClient));
        var noteJson = JsonSerializer.Serialize(note);
        var content = new StringContent(noteJson, Encoding.UTF8, "application/json");
        
        var response = await httpClient.PutAsync($"notes/{id}", content);
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