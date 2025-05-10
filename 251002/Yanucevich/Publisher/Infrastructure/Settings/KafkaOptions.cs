namespace Infrastructure.Settings;

public class KafkaOptions
{
    public string BootstrapServer { get; set; }
    public string ReactionEventsTopic { get; set; }
    public string ReactionResponsesTopic { get; set; }
    public string GroupId { get; set; }
}
