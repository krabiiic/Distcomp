package by.ikrotsyuk.bsuir.modulediscussion.kafka;

public class KafkaConstants {
    public static final String IN_TOPIC_NAME = "in-topic";
    public static final String OUT_TOPIC_NAME = "out-topic";
    public static final int NUMBER_OF_PARTITIONS = 3;
    public static final String BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER = "org.springframework.kafka.support.serializer.JsonSerializer";
    public static final String CONSUMER_GROUP_ID = "in-topic-listener";
    public static final String TRUSTED_PACKAGES = "*";

    public static final String ACKNOWLEDGEMENTS_POLICY = "all";
    public static final String DELIVERY_TIMEOUT_MS = "100000";
    public static final String LINGER_MS = "0";
    public static final String REQUEST_TIMEOUT_MS = "30000";
    public static final String ENABLE_IDEMPOTENCE = "true";
    public static final String MAX_IN_FLIGHT_REQUESTS_PER_SECOND = "5";
}
