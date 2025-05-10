using WebApplication1.DTO;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public interface IUserService
    {
        Task<UserResponseTo> CreateUserAsync(UserRequestTo dto);
        Task<UserResponseTo> GetUserByIdAsync(long id);
        Task<PaginatedResult<UserResponseTo>> GetAllUsersAsync(int pageNumber = 1, int pageSize = 10);
        Task<UserResponseTo> UpdateUserAsync(long id, UserRequestTo dto);
        Task DeleteUserAsync(long id);
    }
}
