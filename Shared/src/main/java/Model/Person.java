package Model;

public class Person {

    private String descendant;
    private String person_id;
    private String first_name;
    private String last_name;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    public Person(String descendant, String person_id, String first_name, String last_name, String gender)
    {
        this.descendant = descendant;
        this.person_id = person_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
    }

    public void setFather(String newF)
    {
        father = newF;
    }

    public void setMother(String newM)
    {
        mother = newM;
    }

    public void setSpouse(String newS)
    {
        spouse = newS;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPerson_id() {
        return person_id;
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

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }
}
