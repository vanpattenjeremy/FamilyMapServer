package Request;

public class EventsRequest {

    private String authToken;

    public EventsRequest(String authToken)
    {
        this.authToken = authToken;
    }

    public String getAuthtoken() {
        return authToken;
    }
}
