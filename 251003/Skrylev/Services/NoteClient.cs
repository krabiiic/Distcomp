using MyApp.Models;
using System.Net.Http;
using System.Net.Http.Json;
using System.Threading.Tasks;

public interface INoteClient
{
    Task<Note> GetNoteAsync(int id);
    Task<Note> CreateNoteAsync(Note note);
    Task<Note> UpdateNoteAsync(int id, Note note);
    Task DeleteNoteAsync(int id);
}

public class NoteClient : INoteClient
{
    private readonly HttpClient _httpClient;
    private const string BaseUrl = "http://localhost:24130/api/v1.0/notes";

    public NoteClient(HttpClient httpClient)
    {
        _httpClient = httpClient;
    }

    public async Task<Note> GetNoteAsync(int id)
    {
        var response = await _httpClient.GetAsync($"{BaseUrl}/{id}");
        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<Note>();
    }

    public async Task<Note> CreateNoteAsync(Note note)
    {
        var response = await _httpClient.PostAsJsonAsync(BaseUrl, note);
        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<Note>();
    }

    public async Task<Note> UpdateNoteAsync(int id, Note note)
    {
        var response = await _httpClient.PutAsJsonAsync($"{BaseUrl}/{id}", note);
        response.EnsureSuccessStatusCode();
        return await response.Content.ReadFromJsonAsync<Note>();
    }

    public async Task DeleteNoteAsync(int id)
    {
        var response = await _httpClient.DeleteAsync($"{BaseUrl}/{id}");
        response.EnsureSuccessStatusCode();
    }
}