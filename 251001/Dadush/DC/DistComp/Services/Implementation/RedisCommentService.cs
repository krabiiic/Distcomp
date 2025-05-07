using DistComp.Models;

namespace DistComp.Services.Implementation {
    public class RedisCommentService : ICommentService {
        public Task<CommentOutDto> Create(CommentInDto data) {
            throw new NotImplementedException();
        }

        public Task Delete(long id) {
            throw new NotImplementedException();
        }

        public Task<CommentOutDto?> Get(long id) {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<CommentOutDto>> GetAll() {
            throw new NotImplementedException();
        }

        public Task<CommentOutDto> Update(Comment data) {
            throw new NotImplementedException();
        }
    }
}
