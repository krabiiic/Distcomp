using Microsoft.EntityFrameworkCore;
using publisher.ToDiscussion;
using publisher.ToDiscussion.DTO;
using publisher.DTO.Request;
using publisher.DTO.Response;
using publisher.Exceptions;
using publisher.Services.Interfaces;
using publisher.Storage;
using Refit;
using Microsoft.AspNetCore.Http;
using Microsoft.Extensions.Logging;
using System.Reflection.Emit;

namespace publisher.Services.Implementations
{
    public sealed class NoteService(IDiscussionService discussionService, AppDbContext dbContext) : INoteService
    {
        private readonly Random _random = new();

        public async Task<NoteResponseTo> GetNoteById(long id) =>
            (await discussionService.GetNote(id)).ToResponse();

        public async Task<IEnumerable<NoteResponseTo>> GetNotes() =>
            (await discussionService.GetNotes()).ToResponse();
        
        public async Task<NoteResponseTo> CreateNote(CreateNoteRequestTo createNoteRequestTo, string country)
        {
            var isArticleExist = await dbContext.Articles.AnyAsync(t => t.Id == createNoteRequestTo.ArticleId);
            if (!isArticleExist)
                throw new EntityNotFoundException("Article not found");

            var noteId = CreateId();

            var discussionRequest = new DiscussionNoteRequestTo(
                Id: noteId,
                ArticleId: createNoteRequestTo.ArticleId,
                Content: createNoteRequestTo.Content,
                Country: country
            );

            try
            {
                var discussionResponse = await discussionService.CreateNote(discussionRequest);
                return discussionResponse.ToResponse();
            }
            catch (ApiException ex)
            {
                throw new Exception($"Failed to create note in discussion: {ex.StatusCode}", ex);
            }
        }

        public async Task DeleteNote(long id)
        {
            await discussionService.DeleteNote(id);
        }
        
        public async Task<NoteResponseTo> UpdateNote(UpdateNoteRequestTo modifiedNote, string country)
        {
            var isArticleExist = await dbContext.Articles.AnyAsync(t => t.Id == modifiedNote.ArticleId);
            if (!isArticleExist)
                throw new EntityNotFoundException("Article not found");

            var discussionRequest = new DiscussionNoteRequestTo(
                Id: modifiedNote.Id,
                ArticleId: modifiedNote.ArticleId,
                Content: modifiedNote.Content,
                Country: country
            );

            try
            {
                return (await discussionService.UpdateNote(discussionRequest)).ToResponse();
            }
            catch (ApiException ex)
            {
                throw new Exception($"Failed to update note in discussion: {ex.StatusCode}", ex);
            }
        }

        private long CreateId() =>
            Math.Abs((DateTimeOffset.UtcNow.ToUnixTimeMilliseconds() << 24 | (uint)_random.Next(0, 1 << 24)) & long.MaxValue);

    }
}
