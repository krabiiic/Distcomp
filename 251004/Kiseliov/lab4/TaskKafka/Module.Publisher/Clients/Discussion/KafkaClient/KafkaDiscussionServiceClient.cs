using Publisher.Clients.Discussion.Dto.Message;
using Publisher.Clients.Discussion.Dto.Request;
using Publisher.Clients.Discussion.Dto.Response;
using TaskKafka.ServiceDefaults.Kafka.SyncClient;
namespace Publisher.Clients.Discussion.KafkaClient;

public class KafkaDiscussionServiceClient(KafkaSyncClient<InTopicMessage, OutTopicMessage> client) : IDiscussionService
{
    public async Task<DiscussionNoticeResponseTo> GetNotice(long id)
    {
        OutTopicMessage outMessage = await client.ProduceAsync(
            new InTopicMessage(OperationType.GetById, new DiscussionNoticeRequestTo(id)));

        if (!outMessage.IsSuccess)
            throw new Exception(outMessage.ErrorMessage);

        return outMessage.Result.First();
    }

    public async Task<IEnumerable<DiscussionNoticeResponseTo>> GetNotices()
    {
        OutTopicMessage outMessage = await client.ProduceAsync(
            new InTopicMessage(OperationType.GetAll, new DiscussionNoticeRequestTo()));

        if (!outMessage.IsSuccess)
            throw new Exception(outMessage.ErrorMessage);

        return outMessage.Result;
    }

    public async Task<DiscussionNoticeResponseTo> CreateNotice(DiscussionNoticeRequestTo createNoticeRequestTo)
    {
        OutTopicMessage outMessage = await client.ProduceAsync(
            new InTopicMessage(OperationType.Create, createNoticeRequestTo));

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

    public async Task<DiscussionNoticeResponseTo> UpdateNotice(DiscussionNoticeRequestTo noticeRequestTo)
    {
        OutTopicMessage outMessage = await client.ProduceAsync(
            new InTopicMessage(OperationType.Update, noticeRequestTo));

        if (!outMessage.IsSuccess)
            throw new Exception(outMessage.ErrorMessage);

        return outMessage.Result.First();
    }
}
