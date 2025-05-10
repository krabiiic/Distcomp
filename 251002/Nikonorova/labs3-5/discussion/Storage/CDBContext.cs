using discussion.Models;
using System.Diagnostics.Metrics;
using Cassandra;
using Cassandra.Data.Linq;
using ISession = Cassandra.ISession;

namespace discussion.Storage
{
    public class CDBContext
    {
        public Table<Note> Notes { get; set; }

        public CDBContext(CDBOptions options)
        {
            ISession session = Connect(options);
            CreateKeyspace(session, options.Keyspace);
            CreateTable(session);

            Notes = new Table<Note>(session);
        }

        private static ISession Connect(CDBOptions options)
        {
            Cluster cluster = Cluster.Builder()
                .AddContactPoint(options.Host)
                .WithPort(options.Port)
                .Build();

            return cluster.Connect();
        }

        private static void CreateKeyspace(ISession session, string keyspace)
        {
            session.Execute($"CREATE KEYSPACE IF NOT EXISTS {keyspace} WITH REPLICATION = " +
                $"{{ 'class' : 'SimpleStrategy', 'replication_factor' : 1 }}");
            session.Execute($"USE {keyspace}");
        }

        private static void CreateTable(ISession session)
        {
            session.Execute(new SimpleStatement(
                $"CREATE TABLE IF NOT EXISTS tbl_notes (" +
                "id BIGINT," +
                "articleId BIGINT," +
                "country TEXT," +
                "content TEXT," +
                "PRIMARY KEY ((country), id, articleId)" +
                ");"
            ));
        }
    }
}
