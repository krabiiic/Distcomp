using WebApplication1.DTO;
using WebApplication1.Entity;
using WebApplication1.Repository;

namespace WebApplication1.Service
{
    public class UserService : IUserService
    {
        private readonly IRepository<User> _userRepo;

        public UserService(IRepository<User> userRepo)
        {
            _userRepo = userRepo;
        }

        public async Task<UserResponseTo> CreateUserAsync(UserRequestTo dto)
        {
            if (string.IsNullOrWhiteSpace(dto.Login) || dto.Login.Length < 3 || dto.Login.Length > 64)
            {
                throw new ValidationException("Login must be between 3 and 64 characters", 400, "40001");
            }
            if (string.IsNullOrWhiteSpace(dto.Password) || dto.Password.Length < 8 || dto.Password.Length > 128)
            {
                throw new ValidationException("Password must be at least 8 characters", 400, "40004");
            }
            if (string.IsNullOrWhiteSpace(dto.Firstname) || dto.Firstname.Length < 3)
            {
                throw new ValidationException("Firstname must be at least 3 characters", 400, "40002");
            }
            if (string.IsNullOrWhiteSpace(dto.Lastname) || dto.Lastname.Length < 3)
            {
                throw new ValidationException("Lastname must be at least 3 characters", 400, "40003");
            }

            var users = await _userRepo.GetAllAsync(1, 1000);
            if (users.Items.Any(u => u.Login.Equals(dto.Login, StringComparison.OrdinalIgnoreCase)))
            {
                throw new ValidationException($"User with login '{dto.Login}' already exists", 403, "40301");
            }

            var user = new User
            {
                Login = dto.Login,
                Password = dto.Password,
                Firstname = dto.Firstname,
                Lastname = dto.Lastname
            };

            var created = await _userRepo.CreateAsync(user);
            return new UserResponseTo
            {
                id = created.Id,
                login = created.Login,
                firstname = created.Firstname,
                lastname = created.Lastname
            };
        }

        public async Task<PaginatedResult<UserResponseTo>> GetAllUsersAsync(int pageNumber = 1, int pageSize = 10)
        {
            var pagedUsers = await _userRepo.GetAllAsync(pageNumber, pageSize);
            var resultDto = new PaginatedResult<UserResponseTo>(
                pagedUsers.Items.Select(u => new UserResponseTo
                {
                    id = u.Id,
                    login = u.Login,
                    firstname = u.Firstname,
                    lastname = u.Lastname
                }),
                pagedUsers.TotalCount,
                pagedUsers.PageNumber,
                pagedUsers.PageSize);
            return resultDto;
        }

        public async Task<UserResponseTo> GetUserByIdAsync(long id)
        {
            var user = await _userRepo.GetByIdAsync(id);
            if (user == null)
            {
                throw new ValidationException($"User with id {id} not found", 404, "40401");
            }
            return new UserResponseTo
            {
                id = user.Id,
                login = user.Login,
                firstname = user.Firstname,
                lastname = user.Lastname
            };
        }

        public async Task<UserResponseTo> UpdateUserAsync(long id, UserRequestTo dto)
        {
            var existing = await _userRepo.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ValidationException($"User with id {id} not found", 404, "40403");
            }
            if (string.IsNullOrWhiteSpace(dto.Login) || dto.Login.Length < 3 || dto.Login.Length > 64)
            {
                throw new ValidationException("Login must be between 3 and 64 characters", 400, "40011");
            }
            if (string.IsNullOrWhiteSpace(dto.Firstname) || dto.Firstname.Length < 3)
            {
                throw new ValidationException("Firstname must be at least 3 characters", 400, "40012");
            }
            if (string.IsNullOrWhiteSpace(dto.Lastname) || dto.Lastname.Length < 3)
            {
                throw new ValidationException("Lastname must be at least 3 characters", 400, "40013");
            }

            if (!existing.Login.Equals(dto.Login, StringComparison.OrdinalIgnoreCase))
            {
                var users = await _userRepo.GetAllAsync(1, 1000);
                if (users.Items.Any(u => u.Login.Equals(dto.Login, StringComparison.OrdinalIgnoreCase)))
                {
                    throw new ValidationException($"User with login '{dto.Login}' already exists", 403, "40302");
                }
            }

            existing.Login = dto.Login;
            existing.Password = dto.Password;
            existing.Firstname = dto.Firstname;
            existing.Lastname = dto.Lastname;

            var updated = await _userRepo.UpdateAsync(existing);
            return new UserResponseTo
            {
                id = updated.Id,
                login = updated.Login,
                firstname = updated.Firstname,
                lastname = updated.Lastname
            };
        }

        public async Task DeleteUserAsync(long id)
        {
            var existing = await _userRepo.GetByIdAsync(id);
            if (existing == null)
            {
                throw new ValidationException($"User with id {id} not found", 404, "40408");
            }
            await _userRepo.DeleteAsync(id);
        }
    }
}
