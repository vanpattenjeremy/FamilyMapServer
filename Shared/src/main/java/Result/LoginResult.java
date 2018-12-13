package Result;

public class LoginResult {

    private String authToken = null;
    private String userName = null;
    private String personID = null;
    private String message = null;

    public LoginResult(String authToken, String userName, String personID)
    {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public LoginResult(String message)
    {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }
}
