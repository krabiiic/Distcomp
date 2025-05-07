using discussion.DTO.Message;
using discussion.DTO.Response;
using discussion.Services.Interfaces;


namespace discussion.Services.Kafka
{
    public sealed class KafkaNoticesHandler(
    KafkaMessageProducer<string, OutTopicMessage> producer,
    INoteService noticeService) : IKafkaHandler<string, InTopicMessage>
    {
        public async Task HandleAsync(string key, InTopicMessage value)
        {
            OutTopicMessage response;
            try
            {
                response = await HandleOperation(value);
            }
            catch (Exception e)
            {
                response = new OutTopicMessage(value.OperationType, [], e.Message);
            }

            await producer.ProduceAsync(key, response);
        }

        private async Task<OutTopicMessage> HandleOperation(InTopicMessage value) => value.OperationType switch
        {
            OperationType.GetById => await HandleGetById(value),
            OperationType.GetAll => await HandleGetAll(),
            OperationType.Create => await HandleCreate(value),
            OperationType.Delete => await HandleDelete(value),
            OperationType.Update => await HandleUpdate(value),
            _ => throw new ArgumentOutOfRangeException()
        };

        private async Task<OutTopicMessage> HandleGetById(InTopicMessage value)
        {
            NoteResponseTo notice = await noticeService.GetNoteById(value.Message.Id);
            return new OutTopicMessage(value.OperationType, [notice]);
        }

        private async Task<OutTopicMessage> HandleGetAll()
        {
            var notices = await noticeService.GetNotes();
            return new OutTopicMessage(OperationType.GetAll, notices.ToList());
        }

        private async Task<OutTopicMessage> HandleCreate(InTopicMessage value)
        {
            NoteResponseTo notice = await noticeService.CreateNote(value.Message);
            return new OutTopicMessage(value.OperationType, [notice]);
        }

        private async Task<OutTopicMessage> HandleDelete(InTopicMessage value)
        {
            await noticeService.DeleteNote(value.Message.Id);
            return new OutTopicMessage(value.OperationType, []);
        }

        private async Task<OutTopicMessage> HandleUpdate(InTopicMessage value)
        {
            NoteResponseTo notice = await noticeService.UpdateNote(value.Message);
            return new OutTopicMessage(value.OperationType, [notice]);
        }
    }

    public class KafkaMessageProducer<T1, T2>
    {
        internal async Task ProduceAsync(string key, OutTopicMessage response)
        {
            throw new NotImplementedException();
        }
    }

    public interface IKafkaHandler<in TK, in TV>
    {
        Task HandleAsync(TK key, TV value);
    }

    

}
