package Model;

public class AuthToken {

    private String token;
    private String username;

    public AuthToken(String token, String username)
    {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public boolean equals(AuthToken a) {
        return (token.equals(a.getToken()) && username.equals(a.getUsername()));
    }
}
