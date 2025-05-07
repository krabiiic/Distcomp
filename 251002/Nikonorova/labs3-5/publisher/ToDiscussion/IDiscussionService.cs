using publisher.ToDiscussion.DTO;
using Refit;

namespace publisher.ToDiscussion
{
    public interface IDiscussionService
    {
        [Get("/notes/{id}")]
        Task<DiscussionNoteResponseTo> GetNote(long id);

        [Get("/notes")]
        Task<IEnumerable<DiscussionNoteResponseTo>> GetNotes();
        [Post("/notes")]
        Task<DiscussionNoteResponseTo> CreateNote([Body] DiscussionNoteRequestTo createNoteRequestTo);

        [Delete("/notes/{id}")]
        Task DeleteNote(long id);

        [Put("/notes")]
        Task<DiscussionNoteResponseTo> UpdateNote([Body] DiscussionNoteRequestTo noteRequestTo);
    }
}
