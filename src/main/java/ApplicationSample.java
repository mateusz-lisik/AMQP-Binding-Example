import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import events.UserLastSeenEvent;
import events.UserProfileUpdatedEvent;
import events.UserStatusChangedEvent;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ApplicationSample {
    // Quick & dirty regex validating Bson ObjectId
    public static final Pattern OBJECT_ID_REGEX = Pattern.compile("(^[0-9a-fA-F]{24}$)");
    private static final EventBus eventBus = new EventBus();
    private final Channel channel;
    private final String userId;


    public ApplicationSample(String userId) throws IOException {
        this.userId = userId;
        channel = createChannel();

        // register this class as event receiver
        eventBus.register(this);
    }

    private Channel createChannel() throws IOException {
        final ConnectionFactory connectionFactory = new ConnectionFactory();

        // YOU NEED TO SET THESE
        connectionFactory.setHost();
        connectionFactory.setUsername();
        connectionFactory.setPassword();
        return connectionFactory.newConnection().createChannel();
    }

    public static void main(String... args) throws IOException {
        String userId = null;

        System.out.println("Hello and welcome to simple AMQP Example");
        while (userId == null) {
            System.out.println("Please state VALID user id that you want to spy:");
            final Scanner scanner = new Scanner(System.in);
            try {
                userId = scanner.next(OBJECT_ID_REGEX);
            } catch (NoSuchElementException e) {
                System.out.println("Provided id is not valid id!");
            }
        }

        final ApplicationSample sample = new ApplicationSample(userId);
        sample.start();
    }

    public void start() throws IOException {
        System.out.println("Starting to listen for events.");

        // Declare temporary queue
        final String temporaryQueue = channel.queueDeclare().getQueue();
        // Bind to all queues we're interested in.
        channel.queueBind(temporaryQueue, "events", EventTopics.STATUS_CHANGED.formatTopic(userId));
        channel.queueBind(temporaryQueue, "events", EventTopics.PROFILE_UPDATED.formatTopic(userId));
        channel.queueBind(temporaryQueue, "events", EventTopics.USER_LAST_SEEN.formatTopic(userId));
        // Initialize our custom consumer
        final SampleConsumer consumer = new SampleConsumer(channel, eventBus);
        channel.basicConsume(temporaryQueue, consumer);
    }

    @Subscribe
    public void onUserProfileUpdated(UserProfileUpdatedEvent event) {
        final String message = event.getClass().getSimpleName() + ": " + event.getJson();
        System.out.println(message);
    }

    @Subscribe
    public void onUserStatusChanged(UserStatusChangedEvent event) {
        final String message = event.getClass().getSimpleName() + ": " + event.getJson();
        System.out.println(message);
    }

    @Subscribe
    public void onUserLastSeen(UserLastSeenEvent event) {
        final String message = event.getClass().getSimpleName() + ": " + event.getJson();
        System.out.println(message);
    }
}
