package Request;

public class FillRequest {

    private String username = null;
    private int generations = 4;

    public FillRequest(String username)
    {
        this.username = username;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public String getUsername() {
        return username;
    }

    public int getGenerations() {
        return generations;
    }
}
