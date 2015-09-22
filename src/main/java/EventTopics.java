import java.util.stream.Stream;

public enum EventTopics {
    STATUS_CHANGED("event.status-changed."),
    PROFILE_UPDATED("event.profile-updated."),
    USER_LAST_SEEN("event.ping."),
    UNKNOWN(null);

    private final String routingKey;

    EventTopics(String routingKey) {
        this.routingKey = routingKey;

    }

    public String getRoutingKey() {
        return routingKey;
    }
    public String formatTopic(String userId) {
        return routingKey + userId;
    }

    /**
     * Returns EventTopics enum for given routingKey string
     * If none found - unknown key will be returned
     *
     * @param topic AMQP Topic
     * @return appropriate enum for given routingKey
     */
    public static EventTopics fromRoutingKey(String topic) {
        return Stream.of(EventTopics.values())
                .filter(eventTopics -> topic.startsWith(eventTopics.getRoutingKey()))
                .findFirst().orElse(EventTopics.UNKNOWN);
    }
}
