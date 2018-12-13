package Request;

public class OnePersonRequest {

    private String authToken;
    private String personID;

    public OnePersonRequest(String authToken, String personID)
    {
        this.authToken = authToken;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getPersonId() {
        return personID;
    }
}
