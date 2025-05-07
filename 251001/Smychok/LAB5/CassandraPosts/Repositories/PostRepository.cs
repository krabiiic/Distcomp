    using Cassandra;
    using Cassandra.Mapping;
    using CassandraPosts.Models;

    namespace CassandraPosts.Repositories;

    public class PostRepository : IPostRepository
    {
        private readonly IMapper _mapper;

        public PostRepository(ICluster cluster)
        {
            var session = cluster.Connect("distcomp");
            _mapper = new Mapper(session);
            
            // Настраиваем маппинг
            MappingConfiguration.Global.Define<PostMapping>();
        }

        public async Task<Post> CreateAsync(Post post)
        {
            await _mapper.InsertAsync(post);
            return post;
        }

        public async Task<Post?> GetByIdAsync(Guid postId)
        {
            try
            {
                return await _mapper.FirstOrDefaultAsync<Post>(
                    "WHERE id = ?", postId);
            }
            catch (Exception ex)
            {
                // Логируем ошибку для диагностики
                Console.WriteLine($"Error getting post by ID: {ex.Message}");
                return null;
            }
        }

        public async Task<IEnumerable<Post>> GetAllAsync()
        {
            return await _mapper.FetchAsync<Post>("SELECT * FROM tbl_post");
        }

        public async Task<Post> UpdateAsync(Post post)
        {
            await _mapper.UpdateAsync(post);
            return post;
        }

        public async Task<bool> DeleteAsync(Guid postId)
        {
            try
            {
                await _mapper.DeleteAsync<Post>("WHERE id = ?", postId);
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }
    }

    // Класс для настройки маппинга
    public class PostMapping : Mappings
    {
        public PostMapping()
        {
            For<Post>()
                .TableName("tbl_post")
                .KeyspaceName("distcomp")
                .PartitionKey(p => p.id)
                .Column(p => p.id, cm => cm.WithName("id").WithDbType<Guid>())
                .Column(p => p.IssueId, cm => cm.WithName("issue_id"))
                .Column(p => p.Content, cm => cm.WithName("content"))
                .Column(p => p.Country, cm => cm.WithName("country"))
                .Column(p => p.State, cm => cm.WithName("state"));
        }
    }