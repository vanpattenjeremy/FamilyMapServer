package Request;

public class PersonsRequest {

    private String authToken;

    public PersonsRequest(String authToken)
    {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }
}
