using Lab3.Core.Contracts;
using Lab3.Core.Models;

namespace Lab3.Core.Abstractions
{
    public interface IMessageRepository
    {
        MessageResponseTo? Get(ulong id);
        MessageResponseTo Create(Message message);
        List<MessageResponseTo> GetAll();
        bool Delete(ulong id);
        MessageResponseTo? Update(Message message, ulong id);
    }
}
