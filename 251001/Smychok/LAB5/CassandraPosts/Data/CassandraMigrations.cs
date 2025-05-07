using Cassandra;

namespace CassandraPosts.Data;

public static class CassandraMigrations
{
    public static void ApplyMigrations(ICluster cluster)
    {
        var session = cluster.Connect(); // Подключаемся без указания keyspace
        
        // Создаем keyspace, если не существует
        session.Execute(@"
            CREATE KEYSPACE IF NOT EXISTS distcomp 
            WITH replication = {
                'class': 'SimpleStrategy',
                'replication_factor': 1
            }");

        // Теперь подключаемся к созданному keyspace
        session = cluster.Connect("distcomp");
        
        // Создаем таблицу с правильной структурой
        session.Execute(@"
            CREATE TABLE IF NOT EXISTS tbl_post (
                id uuid,
                issue_id int,
                content text,
                country text,
                state text,
                PRIMARY KEY (id)
            )");
    }
}