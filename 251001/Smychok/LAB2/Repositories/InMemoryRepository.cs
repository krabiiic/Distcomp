// namespace LAB2.Repositories;
//
// public class InMemoryRepository<T> : IRepository<T> where T : class
// {
//     private readonly List<T> _entities = new List<T>();
//
//     public Task<IEnumerable<T>> GetAllAsync(QueryParams? queryParams = null) => Task.FromResult(_entities.AsEnumerable());
//
//     public Task<T> GetByIdAsync(int id) => Task.FromResult(_entities.FirstOrDefault(e => (e as dynamic).id == id));
//
//     public Task<T> CreateAsync(T entity)
//     {
//         _entities.Add(entity);
//         return Task.FromResult(entity);
//     }
//
//     public Task<T> UpdateAsync(T entity)
//     {
//         var index = _entities.FindIndex(e => (e as dynamic).id == (entity as dynamic).id);
//         if (index != -1) _entities[index] = entity;
//         return Task.FromResult(entity);
//     }
//
//     public Task<bool> DeleteAsync(int id)
//     {
//         var entity = _entities.FirstOrDefault(e => (e as dynamic).id == id);
//         if (entity == null)
//         {
//             return Task.FromResult(false); // Сущность не найдена
//         }
//
//         _entities.Remove(entity);
//         return Task.FromResult(true); // Сущность удалена
//     }
// }