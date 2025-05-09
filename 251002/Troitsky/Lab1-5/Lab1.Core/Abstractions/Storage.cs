using System.Numerics;
namespace Lab1.Core.Abstractions
{
    // in-memory хранилище
    public class Storage<T> where T : IEntity 
    {
        private Dictionary<ulong, T> _items = new Dictionary<ulong, T>();
        private ulong _currentId = 1;
        public T Add(T item)
        {
            ulong newId = _currentId;
            item.Id = newId;
            _items[newId] = item;
            _currentId++;
            return item;
        }
        public T? Get(ulong id)
        {
            return _items.TryGetValue(id, out var item) ? item : default;
        }
        public bool Remove(ulong id)
        {
            return _items.Remove(id);
        }
        public T? Update(T item)
        {
            if (!_items.ContainsKey(item.Id))
            {
                return default;
            }
            _items[item.Id] = item;
            return _items[item.Id];
        }
        public IEnumerable<T> GetAll()
        {
            return _items.Values;
        }
    }
}