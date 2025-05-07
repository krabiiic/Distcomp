namespace Discussion.Storage;

public record CassandraDbOptions(string Host, int Port, string Keyspace);
