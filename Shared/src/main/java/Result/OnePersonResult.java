package Result;

import Model.Person;

public class OnePersonResult {

    private Person person = null;
    private String message = null;

    public OnePersonResult(Person person)
    {
        this.person = person;
    }

    public OnePersonResult(String message)
    {
        this.message = message;
    }

    public Person getPerson() {
        return person;
    }

    public String getMessage() {
        return message;
    }
}
