using Cassandra;
using Core;
using Discussion.abstractions;
using DTO.requests;
using DTO.responces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Discussion.services
{
    public class NoteService : INoteService
    {
        private readonly Cassandra.ISession _session;

        public NoteService(IConfiguration configuration)
        {
            var contactPoint = configuration["Cassandra:ContactPoint"];
            var port = int.Parse(configuration["Cassandra:Port"]);
            var keyspace = configuration["Cassandra:Keyspace"];

            var cluster = Cluster.Builder()
                .AddContactPoint(contactPoint)
                .WithPort(port)
                .Build();

            // Ensure keyspace exists
            var sysSession = cluster.Connect();
            sysSession.Execute($@"
                CREATE KEYSPACE IF NOT EXISTS {keyspace} 
                WITH replication = {{'class': 'SimpleStrategy', 'replication_factor': 1}};
            ");

            _session = cluster.Connect(keyspace);

            _session.Execute(@"
                CREATE TABLE IF NOT EXISTS tbl_notes (
                    id bigint PRIMARY KEY,
                    storyid bigint,
                    content text
                );
            ");
        }

        public async Task<NoteResponseTo?> AddNoteRespAsync(NoteRequestTo note)
        {
            var id = DateTimeOffset.UtcNow.ToUnixTimeMilliseconds();

            var query = _session.Prepare(@"
                INSERT INTO tbl_notes (id, storyid, content)
                VALUES (?, ?, ?)
                ");

            await _session.ExecuteAsync(query.Bind(id, note.StoryId, note.Content));

            return new NoteResponseTo
            {
                Id = id,
                StoryId = note.StoryId,
                Content = note.Content
            };
        }

        public async Task<NoteResponseTo?> AddNoteAsync(Note note)
        {
            var query = _session.Prepare(@"
                INSERT INTO tbl_notes (id, storyid, content)
                VALUES (?, ?, ?)
                ");

            await _session.ExecuteAsync(query.Bind(note.Id, note.StoryId, note.Content));

            return new NoteResponseTo
            {
                Id = note.Id,
                StoryId = note.StoryId,
                Content = note.Content
            };
        }

        public async Task<bool> DeleteNoteAsync(long id)
        {
            var query = _session.Prepare("DELETE FROM tbl_notes WHERE id = ?");
            await _session.ExecuteAsync(query.Bind(id));
            return true;
        }

        public async Task<IEnumerable<NoteResponseTo>> GetNotesByStoryIdAsync(long storyId)
        {
            var result = new List<Note>();

            // Cassandra требует, чтобы поле фильтрации было индексировано или частью PRIMARY KEY
            var stmt = new SimpleStatement("SELECT * FROM tbl_notes WHERE storyid = ?", storyId);
            var rows = await _session.ExecuteAsync(stmt);

            foreach (var row in rows)
            {
                result.Add(new Note
                {
                    Id = row.GetValue<long>("id"),
                    StoryId = row.GetValue<long>("storyid"),
                    Content = row.GetValue<string>("content")
                });
            }
            var res = result.Select(n => new NoteResponseTo { Id = n.Id, Content = n.Content, StoryId = n.StoryId });
            return res;
        }

        public async Task<IEnumerable<NoteResponseTo>> GetAllAsync()
        {
            var result = new List<Note>();
            var stmt = new SimpleStatement("SELECT * FROM tbl_notes");
            var rows = await _session.ExecuteAsync(stmt);

            foreach (var row in rows)
            {
                result.Add(new Note
                {
                    Id = row.GetValue<long>("id"),
                    StoryId = row.GetValue<long>("storyid"),
                    Content = row.GetValue<string>("content")
                });
            }
            var res = result.Select(n => new NoteResponseTo { Id = n.Id, Content = n.Content, StoryId = n.StoryId });
            return res;
        }

        public async Task<NoteResponseTo?> GetByIdAsync(long id)
        {
            var stmt = new SimpleStatement("SELECT * FROM tbl_notes WHERE id = ?", id);
            var row = await _session.ExecuteAsync(stmt);

            var noteRow = row.FirstOrDefault();
            if (noteRow == null)
                return null;

            return new NoteResponseTo
            {
                Id = noteRow.GetValue<long>("id"),
                StoryId = noteRow.GetValue<long>("storyid"),
                Content = noteRow.GetValue<string>("content")
            };
        }

        public async Task<bool> UpdateAsync(long id, NoteRequestTo request)
        {
            var query = _session.Prepare(@"
                UPDATE tbl_notes 
                SET storyid = ?, content = ? 
                WHERE id = ?
                ");
            await _session.ExecuteAsync(query.Bind(request.StoryId, request.Content, id));
            return true;
        }
    }
}
