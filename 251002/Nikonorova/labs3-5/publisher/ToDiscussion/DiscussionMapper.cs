using publisher.DTO.Request;
using publisher.ToDiscussion.DTO;
using Riok.Mapperly.Abstractions;
using publisher.DTO.Response;


namespace publisher.ToDiscussion
{
    [Mapper]
    public static partial class DiscussionMapper
    {
        public static DiscussionNoteRequestTo ToDiscussionRequest(this CreateNoteRequestTo requestTo, long id, string country) =>
            new(id, requestTo.ArticleId, requestTo.Content, country);
        public static DiscussionNoteRequestTo ToDiscussionRequest(this UpdateNoteRequestTo requestTo, string country) =>
        new(requestTo.Id, requestTo.ArticleId, requestTo.Content, country);

        public static partial NoteResponseTo ToResponse(this DiscussionNoteResponseTo responseTo);

        public static partial IEnumerable<NoteResponseTo> ToResponse(this IEnumerable<DiscussionNoteResponseTo> responseTo);
    }
}
