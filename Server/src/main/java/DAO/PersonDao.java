package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.DoesNotBelongException;
import Exceptions.InternalServerException;
import Exceptions.InvalidIDException;
import Model.Person;

public class PersonDao extends DaoInterface{

    public PersonDao(Connection conn)
    {
        super(conn);
    }

    public void createPerson(Person newPerson) throws InternalServerException
    {
        try {
            String sql = "INSERT INTO Persons(descendant, person_id, first_name, last_name, gender, father, mother, spouse) VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1, newPerson.getDescendant());
            stmt.setString(2, newPerson.getPerson_id());
            stmt.setString(3, newPerson.getFirst_name());
            stmt.setString(4, newPerson.getLast_name());
            stmt.setString(5, newPerson.getGender());
            stmt.setString(6, newPerson.getFather());
            stmt.setString(7, newPerson.getMother());
            stmt.setString(8, newPerson.getSpouse());

            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Could not add the person to Persons table in the database");
        }
    }

    public ArrayList<Person> getPersons(String descendant) throws InternalServerException
    {
        try{
            String sql = "SELECT * FROM Persons WHERE descendant = ?";

            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,descendant);

            ResultSet rs = stmt.executeQuery();

            ArrayList<Person> persons = new ArrayList<Person>();

            while(rs.next())
            {
                String person_id = rs.getString("person_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String gender = rs.getString("gender");
                String father = rs.getString("father");
                String mother = rs.getString("mother");
                String spouse = rs.getString("spouse");
                Person p = new Person(descendant,person_id,first_name,last_name,gender);
                p.setFather(father);
                p.setMother(mother);
                p.setSpouse(spouse);

                persons.add(p);
            }
            return persons;
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Could not retrieve persons from the Persons table");
        }

    }

    public Person getOnePerson(String username, String person_id) throws InternalServerException, InvalidIDException, DoesNotBelongException
    {
        try{
            String sql = "SELECT * FROM Persons WHERE person_id = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);

            stmt.setString(1,person_id);
            ResultSet rs = stmt.executeQuery();

            if(!rs.next())
            {
                throw new InvalidIDException("Could not find person_id");
            }
            if(!username.equals(rs.getString("descendant")))
            {
                throw new DoesNotBelongException("That person does not belong to the current user");
            }

            String first_name = rs.getString("first_name");
            String last_name = rs.getString("last_name");
            String gender = rs.getString("gender");
            String father = rs.getString("father");
            String mother = rs.getString("mother");
            String spouse = rs.getString("spouse");
            Person p = new Person(username,person_id,first_name,last_name,gender);
            p.setFather(father);
            p.setMother(mother);
            p.setSpouse(spouse);

            return p;
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to get Person from Persons table");
        }
    }


    public void clearTable() throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Persons";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to clear Persons table");
        }
    }

    public void clearTable(String username) throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Persons WHERE descendant = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,username);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to delete persons for the given user");
        }
    }
}
