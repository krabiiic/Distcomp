using AutoMapper;
using DistComp.Data;
using DistComp.Models;
using Microsoft.EntityFrameworkCore;
using Redis.OM;
using Redis.OM.Searching;

namespace DistComp.Services.Implementation {
    public class UserService : ICommonAsyncService<long, User, UserInDto, UserOutDto> {

        private DCContext context;
        private RedisConnectionProvider provider;
        private IRedisCollection<UserOutDto> users;
        private IMapper mapper;

        public UserService(DCContext context, RedisConnectionProvider provider, IMapper mapper) {
            this.context = context;
            this.provider = provider;
            this.mapper = mapper;

            users = provider.RedisCollection<UserOutDto>();
        }

        public async Task<IEnumerable<UserOutDto>> GetAll() {
            var cached = users.ToList();
            if (cached.Count > 0) {
                return cached;
            }

            return (await context.Users.ToListAsync()).Select(e => mapper.Map<UserOutDto>(e));
        }

        public async Task<UserOutDto?> Get(long id) {
            var cached = users.FindById(id.ToString());
            if (cached is not null) {
                return cached;
            }

            var user = await context.Users.FindAsync(id);
            if (user == null) {
                return null;
            } else {
                return mapper.Map<UserOutDto>(user);
            }
        }

        public async Task<UserOutDto> Create(UserInDto data) {
            User newUser = mapper.Map<User>(data);
            await context.Users.AddAsync(newUser);
            await context.SaveChangesAsync();

            var userOut = mapper.Map<UserOutDto>(newUser);

            users.Insert(userOut);

            return userOut;
        }

        public async Task<UserOutDto> Update(User data) {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Id == data.Id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            user.Login = data.Login;
            user.Password = data.Password;
            user.Firstname = data.Firstname;
            user.Lastname = data.Lastname;
            await context.SaveChangesAsync();

            var userOut = mapper.Map<UserOutDto>(user);

            users.Update(userOut);

            return userOut;
        }

        public async Task Delete(long id) {
            var user = await context.Users.FirstOrDefaultAsync(u => u.Id == id)
                ?? throw new KeyNotFoundException("Entity with specified ID does not exist");

            context.Users.Remove(user);
            await context.SaveChangesAsync();

            provider.Connection.Unlink($"{nameof(User)}:{id}");
        }
    }
}
