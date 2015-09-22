package events;

public class UserProfileUpdatedEvent extends AbstractUserEvent {
    public UserProfileUpdatedEvent(String json) {
        super(json);
    }
}
