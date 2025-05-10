using Cassandra.Data.Linq;
using Discussion.Models;
namespace Discussion.Storage;

public sealed class CassandraDbContext(CassandraConnectionFactory connectionFactory) : IDisposable
{
    public Table<Notice> Notices { get; set; } = new(connectionFactory.Connect());
    
    public void Dispose() => Notices.GetSession().Dispose();
}
