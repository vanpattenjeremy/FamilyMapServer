package Result;

public class RegisterResult {

    String authToken = null;
    String userName = null;
    String personID = null;
    String message = null;

    public RegisterResult(String authToken, String userName, String personID)
    {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
    }

    public RegisterResult(String message)
    {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }
}
