package Model;


public class User {

    private String username;
    private String password;
    private String email;
    private String first_name;
    private String last_name;
    private String gender;
    private String person_id;

    public User(String username, String password, String email, String first_name, String last_name, String gender, String person_id)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.person_id = person_id;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getPerson_id() {
        return person_id;
    }

    public boolean equals(User u)
    {
        if(username.equals(u.getUsername()) && password.equals(u.getPassword()) && email.equals(u.getEmail()) && first_name.equals(u.getFirst_name()) && last_name.equals(u.getLast_name())
                && gender.equals(u.getGender()) && person_id.equals(u.getPerson_id()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
