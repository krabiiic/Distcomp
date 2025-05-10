namespace WebApi.Settings;

public class KafkaSettings
{
    public string BootstrapServer { get; set; }
    public string PostEventsTopic { get; set; }
    public string PostResponsesTopic { get; set; }
    public string GroupId { get; set; }
}