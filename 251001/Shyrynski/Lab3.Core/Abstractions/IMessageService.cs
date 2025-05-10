using Lab3.Core.Contracts;
using Lab3.Core.Models;

namespace Lab3.Core.Abstractions
{
    public interface IMessageService
    {
        MessageResponseTo? GetMessage(ulong id);
        MessageResponseTo CreateMessage(Message msg);
        bool DeleteMessage(ulong id);
        List<MessageResponseTo> GetAllMessages();
        MessageResponseTo? UpdateMessage(Message msg, ulong id);
    }
}
