package Result;

import Model.Event;

public class OneEventResult {

    private Event event;
    private String message;

    public OneEventResult(Event event)
    {
        this.event = event;
    }

    public OneEventResult(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Event getEvent() {
        return event;
    }
}
