package events;

public class UserStatusChangedEvent extends AbstractUserEvent {
    public UserStatusChangedEvent(String json) {
        super(json);
    }
}
