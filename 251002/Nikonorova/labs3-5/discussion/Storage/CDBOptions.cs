namespace discussion.Storage
{
    public class CDBOptions
    {
        public CDBOptions(string connectionString)
        {
            ParseConnectionString(connectionString, out var host, out var port, out var keyspace);
            Host = host;
            Port = port;
            Keyspace = keyspace;
        }

        public string Host { get; }
        public int Port { get; }
        public string Keyspace { get; }

        private static void ParseConnectionString(string connectionString, out string host, out int port, out string keyspace)
        {
            var args = connectionString.Split(';');
            host = args[0].Split('=')[1];
            port = int.Parse(args[1].Split('=')[1]);
            keyspace = args[2].Split('=')[1];
        }
    }
}
