package Request;

public class OneEventRequest {

    private String authToken;
    private String eventID;

    public OneEventRequest(String authToken, String eventID)
    {
        this.authToken = authToken;
        this.eventID = eventID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEventId() {
        return eventID;
    }
}
