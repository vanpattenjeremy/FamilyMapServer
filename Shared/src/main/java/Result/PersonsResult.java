package Result;

import java.util.ArrayList;

import Model.Person;

public class PersonsResult {

    private ArrayList<Person> data = null;
    private String message = null;

    public PersonsResult(ArrayList<Person> data)
    {
        this.data = data;
    }

    public PersonsResult(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
