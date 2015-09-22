package events;

public class UserLastSeenEvent extends AbstractUserEvent {
    public UserLastSeenEvent(String json) {
        super(json);
    }
}
