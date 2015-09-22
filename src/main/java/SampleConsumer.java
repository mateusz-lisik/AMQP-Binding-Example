import com.google.common.eventbus.EventBus;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import events.*;

import java.io.IOException;

public class SampleConsumer extends DefaultConsumer {
    private final EventBus eventBus;

    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel  the channel to which this consumer is attached
     * @param eventBus bus that event will be placed to
     */
    public SampleConsumer(Channel channel, EventBus eventBus) {
        super(channel);
        this.eventBus = eventBus;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

        AbstractUserEvent event;
        final String messageBody = new String(body, "UTF-8");
        switch (EventTopics.fromRoutingKey(envelope.getRoutingKey())) {
            case STATUS_CHANGED:
                event = new UserStatusChangedEvent(messageBody);
                break;
            case PROFILE_UPDATED:
                event = new UserProfileUpdatedEvent(messageBody);
                break;
            case USER_LAST_SEEN:
                event = new UserLastSeenEvent(messageBody);
                break;
            default:
                event = new UnsupportedEvent(messageBody);
        }


        eventBus.post(event);
        getChannel().basicAck(envelope.getDeliveryTag(), false);
        super.handleDelivery(consumerTag, envelope, properties, body);
    }
}
