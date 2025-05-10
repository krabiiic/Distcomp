using Cassandra;
using Cassandra.Mapping;
using Discussion.Config;
using Discussion.Models;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace Discussion.Repositories
{
    public class NoteRepository : INoteRepository
    {
        private readonly Cassandra.ISession _session;

        private readonly IMapper _mapper;

        public NoteRepository(CassandraSettings settings)
        {
            var cluster = Cluster.Builder()
                .AddContactPoint(settings.ContactPoint)
                .WithPort(settings.Port)
                .Build();
            _session = cluster.Connect(settings.Keyspace);
            _mapper = new Mapper(_session);
        }

        public async Task<IEnumerable<Note>> GetAllAsync()
        {
            return await _mapper.FetchAsync<Note>("SELECT * FROM tbl_note");
        }

        public async Task<Note?> GetByIdAsync(long id)
        {
            return await _mapper.FirstOrDefaultAsync<Note>(
                "SELECT * FROM tbl_note WHERE id = ? ALLOW FILTERING", id);
        }

        public async Task CreateAsync(Note note)
        {
            await _mapper.InsertAsync(note);
        }

        public async Task UpdateAsync(long id, Note note)
        {
            string cql = @"UPDATE tbl_note 
                           SET content = ?, modified = ?
                           WHERE country = ? AND articleid = ? AND id = ?";
            await _mapper.ExecuteAsync(cql,
                note.Content,
                note.Modified,
                note.Country,
                note.ArticleId,
                id);
        }

        public async Task DeleteAsync(long id)
        {
            var note = await GetByIdAsync(id);
            if (note != null)
            {
                await _mapper.DeleteAsync<Note>("WHERE country = ? AND articleid = ? AND id = ?",
                    note.Country, note.ArticleId, id);
            }
        }
    }
}
