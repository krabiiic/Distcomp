using discussion.Storage;

namespace discussion.Extensions
{
    public static class CDBContextExtensions
    {
        public static void AddCassandraDbContext(
        this IHostApplicationBuilder builder,
        string connectionName)
        {
            var connectionString = builder.Configuration.GetConnectionString(connectionName);

            if (string.IsNullOrEmpty(connectionString))
                throw new ArgumentException("Connection string is not found");

            builder.Services.AddSingleton(new CDBOptions(connectionString));
            builder.Services.AddSingleton<CDBContext>();
        }
    }
}
