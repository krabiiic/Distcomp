public enum OperationType
{
    Create,
    GetById,
    GetAll,
    Update,
    Delete,
}

public class ReactionRequest
{
    public string CorrelationId { get; set; }
    public OperationType OperationType { get; set; }
    public object Payload { get; set; }
}
