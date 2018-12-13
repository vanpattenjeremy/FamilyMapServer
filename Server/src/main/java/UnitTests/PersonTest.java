package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.PersonDao;
import Model.AuthToken;
import Model.Person;
import Request.OnePersonRequest;
import Request.PersonsRequest;
import Result.OnePersonResult;
import Result.PersonsResult;
import Service.ClearService;
import Service.PersonService;

import static org.junit.Assert.assertEquals;

public class PersonTest {
    @Test
    public void testGetPersons()
    {
        try {
            System.out.println("Testing getPersons on an empty Persons table");
            ClearService clearService = new ClearService();
            clearService.clear();
            Database database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            AuthToken at = new AuthToken("blah","Jeremy");
            authTokenDao.addAuthToken(at);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        PersonsRequest request = new PersonsRequest("blah");
        PersonService personService = new PersonService();
        PersonsResult result = personService.getPersons(request);
        assertEquals("No persons found for that user", result.getMessage());
        System.out.println("Passed");

        try{
            System.out.println("Testing getPersons on an Persons table with one person");
            Database database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            Person p = new Person("Jeremy", "blah", "Jeremy", "Van Patten", "m");
            p.setFather("Dad");
            p.setMother("Mom");
            p.setSpouse("Wife");
            personDao.createPerson(p);

            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = personService.getPersons(request);
        ArrayList<Person> persons = result.getData();
        Person person = persons.get(0);
        assertEquals("Jeremy", person.getDescendant());
        assertEquals("blah",person.getPerson_id());
        assertEquals("Jeremy", person.getFirst_name());
        assertEquals("Van Patten", person.getLast_name());
        assertEquals("m", person.getGender());
        assertEquals("Dad", person.getFather());
        assertEquals("Mom", person.getMother());
        assertEquals("Wife", person.getSpouse());

        System.out.println("Passed");

        try{
            System.out.println("Testing getPersons on an Persons table with multiple persons for one user, one with no mother");
            Database database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            Person p = new Person("Jeremy","meh","Natsu","Dragneel","m");
            p.setFather("Igneel");
            p.setSpouse("Lucy");
            personDao.createPerson(p);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = personService.getPersons(request);
        persons = result.getData();
        person = persons.get(0);
        assertEquals("Jeremy", person.getDescendant());
        assertEquals("blah",person.getPerson_id());
        assertEquals("Jeremy",person.getFirst_name());
        assertEquals("Van Patten",person.getLast_name());
        assertEquals("m",person.getGender());
        assertEquals("Dad",person.getFather());
        assertEquals("Mom",person.getMother());
        assertEquals("Wife",person.getSpouse());
        person = persons.get(1);
        assertEquals("Jeremy", person.getDescendant());
        assertEquals("meh",person.getPerson_id());
        assertEquals("Natsu",person.getFirst_name());
        assertEquals("Dragneel",person.getLast_name());
        assertEquals("m",person.getGender());
        assertEquals("Igneel",person.getFather());
        assertEquals(null,person.getMother());
        assertEquals("Lucy",person.getSpouse());

        System.out.println("Passed");
    }

    @Test
    public void testGetPerson()
    {
        try {
            System.out.println("Testing getPerson on an empty Persons table");
            ClearService clearService = new ClearService();
            clearService.clear();
            Database database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            AuthToken at = new AuthToken("blah","Jeremy");
            authTokenDao.addAuthToken(at);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        OnePersonRequest request = new OnePersonRequest("blah", "blah");
        PersonService personService = new PersonService();
        OnePersonResult result = personService.getPerson(request);
        assertEquals("Could not find person_id", result.getMessage());
        System.out.println("Passed");

        try{
            System.out.println("Testing getPerson on an Persons table with one person");
            Database database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            Person p = new Person("Jeremy", "blah", "Jeremy", "Van Patten", "m");
            p.setFather("Dad");
            p.setMother("Mom");
            p.setSpouse("Wife");
            personDao.createPerson(p);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = personService.getPerson(request);
        Person person = result.getPerson();
        assertEquals("Jeremy", person.getDescendant());
        assertEquals("blah",person.getPerson_id());
        assertEquals("Jeremy",person.getFirst_name());
        assertEquals("Van Patten",person.getLast_name());
        assertEquals("m",person.getGender());
        assertEquals("Dad",person.getFather());
        assertEquals("Mom",person.getMother());
        assertEquals("Wife",person.getSpouse());

        System.out.println("Passed");

        try{
            System.out.println("Testing getPerson on an Persons table with multiple persons for one user");
            Database database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            Person p = new Person("Jeremy","meh","Natsu","Dragneel","m");
            p.setFather("Igneel");
            p.setSpouse("Lucy");
            personDao.createPerson(p);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = personService.getPerson(request);
        person = result.getPerson();
        assertEquals("Jeremy", person.getDescendant());
        assertEquals("blah",person.getPerson_id());
        assertEquals("Jeremy",person.getFirst_name());
        assertEquals("Van Patten",person.getLast_name());
        assertEquals("m",person.getGender());
        assertEquals("Dad",person.getFather());
        assertEquals("Mom",person.getMother());
        assertEquals("Wife",person.getSpouse());

        System.out.println("Passed");
    }
}
