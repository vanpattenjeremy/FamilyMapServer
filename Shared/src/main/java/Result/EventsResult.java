package Result;

import java.util.ArrayList;

import Model.Event;

public class EventsResult {

    private ArrayList<Event> data;
    private String message;

    public EventsResult(ArrayList<Event> data)
    {
        this.data = data;
    }

    public EventsResult(String message)
    {
        this.message = message;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
