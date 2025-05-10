using publisher.DTO.Message;
using publisher.ToDiscussion.DTO;
using publisher.ToDiscussion;
using Refit;

namespace publisher.DTO
{
    public class KafkaDiscussionServiceClient(KafkaSyncClient<InTopicMessage, OutTopicMessage> client) : IDiscussionService
    {
        public async Task<DiscussionNoteResponseTo> GetNotice(long id)
        {
            OutTopicMessage outMessage = await client.ProduceAsync(
                new InTopicMessage(OperationType.GetById, new DiscussionNoteRequestTo(id)));

            if (!outMessage.IsSuccess)
                throw new Exception(outMessage.ErrorMessage);

            return outMessage.Result.First();
        }

        public async Task<IEnumerable<DiscussionNoteResponseTo>> GetNotices()
        {
            OutTopicMessage outMessage = await client.ProduceAsync(
                new InTopicMessage(OperationType.GetAll, new DiscussionNoteRequestTo()));

            if (!outMessage.IsSuccess)
                throw new Exception(outMessage.ErrorMessage);
            return outMessage.Result;
        }

        public async Task<DiscussionNoteResponseTo> CreateNotice(DiscussionNoteRequestTo createNoteRequestTo)
        {
            OutTopicMessage outMessage = await client.ProduceAsync(
                new InTopicMessage(OperationType.Create, createNoteRequestTo));

            if (!outMessage.IsSuccess)
                throw new Exception(outMessage.ErrorMessage);

            return outMessage.Result.First();
        }

        public async Task DeleteNotice(long id)
        {
            OutTopicMessage outMessage = await client.ProduceAsync(
                new InTopicMessage(OperationType.Delete, new DiscussionNoticeRequestTo(id)));

            if (!outMessage.IsSuccess)
                throw new Exception(outMessage.ErrorMessage);
        }

        public async Task<DiscussionNoteResponseTo> UpdateNotice(DiscussionNoteRequestTo noticeRequestTo)
        {
            OutTopicMessage outMessage = await client.ProduceAsync(
                new InTopicMessage(OperationType.Update, noticeRequestTo));

            if (!outMessage.IsSuccess)
                throw new Exception(outMessage.ErrorMessage);

            return outMessage.Result.First();
        }

        public Task<DiscussionNoteResponseTo> GetNote(long id)
        {
            throw new NotImplementedException();
        }

        public Task<IEnumerable<DiscussionNoteResponseTo>> GetNotes()
        {
            throw new NotImplementedException();
        }

        public Task<DiscussionNoteResponseTo> CreateNote([Body] DiscussionNoteRequestTo createNoteRequestTo)
        {
            throw new NotImplementedException();
        }

        public Task DeleteNote(long id)
        {
            throw new NotImplementedException();
        }

        public Task<DiscussionNoteResponseTo> UpdateNote([Body] DiscussionNoteRequestTo noteRequestTo)
        {
            throw new NotImplementedException();
        }
    }

    public class KafkaSyncClient<T1, T2>
    {
        internal async Task<OutTopicMessage> ProduceAsync(InTopicMessage inTopicMessage)
        {
            throw new NotImplementedException();
        }
    }
}
