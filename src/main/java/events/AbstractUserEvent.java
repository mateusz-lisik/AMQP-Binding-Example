package events;

/**
 * Since it doesn't make sense to write full json decoding stuff we will only use raw string
 */
public abstract class AbstractUserEvent {
    private final String json;

    protected AbstractUserEvent(String json) {
        this.json = json;

    }

    public String getJson() {
        return json;
    }
}
