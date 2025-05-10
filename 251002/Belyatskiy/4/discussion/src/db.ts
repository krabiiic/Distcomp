import { Client } from 'cassandra-driver';

export const cassandraClient = new Client({
    contactPoints: ['localhost'],
    localDataCenter: 'datacenter1',
    protocolOptions: { port: 9042 },
});

(async () => {
    try {
        await cassandraClient.connect();
        console.log('Connected to Cassandra');

        // Создание keyspace и таблицы
        await cassandraClient.execute(
            "CREATE KEYSPACE IF NOT EXISTS distcomp WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1}"
        );
        await cassandraClient.execute('USE distcomp');
        await cassandraClient.execute(
            'CREATE TABLE IF NOT EXISTS tbl_message (id int PRIMARY KEY, content text, articleId int)'
        );
        console.log('Cassandra schema initialized');
    } catch (error) {
        console.error('Cassandra connection or initialization error:', error);
        process.exit(1);
    }
})();