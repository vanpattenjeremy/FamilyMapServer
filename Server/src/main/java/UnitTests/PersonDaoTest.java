package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import DAO.Database;
import DAO.PersonDao;
import Exceptions.InternalServerException;
import Model.Person;

import static org.junit.Assert.assertEquals;

public class PersonDaoTest {

    @Test
    public void testGetPersons()
    {
        Database database = null;
        String newID = null;
        try{
            System.out.println("Testing get persons on empty Persons table");
            database = new Database();

            PersonDao personDao = new PersonDao(database.openConnection());
            personDao.clearTable();
            personDao.getPersons("Jeremy");

            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            assertEquals("Could not retrieve persons from the Persons table",e.getMessage());
        }

        System.out.println("Passed");

        try{
            database.closeConnection(true);
            System.out.println("Testing get persons with a single person in the Persons table");
            addPersonToDatabaseHelper(database);
            PersonDao personDao = new PersonDao(database.openConnection());
            ArrayList<Person> persons = personDao.getPersons("Jeremy");
            database.closeConnection(true);
            assertEquals(1,persons.size());
            Person person = persons.get(0);
            assertEquals("Jeremy", person.getDescendant());
            assertEquals("blah",person.getPerson_id());
            assertEquals("Jeremy",person.getFirst_name());
            assertEquals("Van Patten",person.getLast_name());
            assertEquals("m",person.getGender());
            assertEquals("Dad",person.getFather());
            assertEquals("Mom",person.getMother());
            assertEquals("Wife",person.getSpouse());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }


        System.out.println("Passed");

        try {
            System.out.println("Testing get multiple persons");
            addMultiplePersonsToDatabaseHelper(database);
            PersonDao personDao = new PersonDao(database.openConnection());
            ArrayList<Person> persons = personDao.getPersons("Jeremy");
            database.closeConnection(true);
            assertEquals(2,persons.size());
            Person person = persons.get(0);
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
            assertEquals("Bob",person.getFirst_name());
            assertEquals("Sagat",person.getLast_name());
            assertEquals("m",person.getGender());
            assertEquals("Dad",person.getFather());
            assertEquals("Mom",person.getMother());
            assertEquals("Waifu",person.getSpouse());
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    @Test
    public void testGetOnePerson()
    {
        Database database = null;
        String newId = null;
        try {
            System.out.println("Testing getting one person on empty Persons table (Person does not exist)");
            database = new Database();

            PersonDao personDao = new PersonDao(database.openConnection());
            personDao.clearTable();
            personDao.getOnePerson("Jeremy","blah");

            database.closeConnection(true);
        }
        catch(Exception e)
        {
            assertEquals("Could not find person_id", e.getMessage());
        }

        System.out.println("Passed");

        try{
            database.closeConnection(true);
            System.out.println("Testing getting one person on a Persons table with the existing person and correct username");
            addPersonToDatabaseHelper(database);

            PersonDao personDao = new PersonDao(database.openConnection());
            Person person = personDao.getOnePerson("Jeremy","blah");
            database.closeConnection(true);

            assertEquals("Jeremy", person.getDescendant());
            assertEquals("blah",person.getPerson_id());
            assertEquals("Jeremy",person.getFirst_name());
            assertEquals("Van Patten",person.getLast_name());
            assertEquals("m",person.getGender());
            assertEquals("Dad",person.getFather());
            assertEquals("Mom",person.getMother());
            assertEquals("Wife",person.getSpouse());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing getting one person on a table with the correct person_id, but incorrect username");
            PersonDao personDao = new PersonDao(database.openConnection());
            Person person = personDao.getOnePerson("Bob", "blah");

            database.closeConnection(true);

            System.out.println("Got and person for Bob when none should exist");
            Assert.fail();
        }
        catch(Exception e)
        {
            assertEquals("That person does not belong to the current user", e.getMessage());
        }

        System.out.println("Passed");
        try{
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println("Could not close database");
            Assert.fail();
        }


    }

    @Test
    public void testCreatePerson()
    {
        Database database = null;
        String newToken = null;
        try{
            System.out.println("Testing adding a new Person");
            database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            personDao.clearTable();

            String descendant = "Jeremy";
            String person_id = "blah";
            String first_name = "Jeremy";
            String last_name = "Van Patten";
            String gender = "m";

            Person p = new Person(descendant,person_id,first_name,last_name,gender);
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
        try{
            PersonDao personDao = new PersonDao(database.openConnection());
            ArrayList<Person> persons = personDao.getPersons("Jeremy");
            database.closeConnection(true);
            Person person = persons.get(0);
            assertEquals("Jeremy", person.getDescendant());
            assertEquals("blah",person.getPerson_id());
            assertEquals("Jeremy",person.getFirst_name());
            assertEquals("Van Patten",person.getLast_name());
            assertEquals("m",person.getGender());
            assertEquals("Dad",person.getFather());
            assertEquals("Mom",person.getMother());
            assertEquals("Wife",person.getSpouse());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding an person with same information, but different person_id");

            PersonDao personDao = new PersonDao(database.openConnection());

            String descendant = "Jeremy";
            String person_id = "meh";
            String first_name = "Jeremy";
            String last_name = "Van Patten";
            String gender = "m";

            Person p = new Person(descendant,person_id,first_name,last_name,gender);
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
        try{
            PersonDao personDao = new PersonDao(database.openConnection());
            ArrayList<Person> persons = personDao.getPersons("Jeremy");
            database.closeConnection(true);
            Person person = persons.get(1);
            assertEquals("Jeremy", person.getDescendant());
            assertEquals("meh",person.getPerson_id());
            assertEquals("Jeremy",person.getFirst_name());
            assertEquals("Van Patten",person.getLast_name());
            assertEquals("m",person.getGender());
            assertEquals("Dad",person.getFather());
            assertEquals("Mom",person.getMother());
            assertEquals("Wife",person.getSpouse());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding an person with different information, but same person_id");
            PersonDao personDao = new PersonDao(database.openConnection());

            String descendant = "Jeremy";
            String person_id = "meh";
            String first_name = "Jeremy";
            String last_name = "Van Patten";
            String gender = "m";

            Person p = new Person(descendant,person_id,first_name,last_name,gender);
            p.setFather("Dad");
            p.setMother("Mom");
            p.setSpouse("Wife");

            personDao.createPerson(p);
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            assertEquals("Could not add the person to Persons table in the database",e.getMessage());
        }

        try {
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");
    }

    @Test
    public void testClearTable()
    {
        try{
            System.out.println("Testing clear on Persons table with data");
            Database database = new Database();
            addPersonToDatabaseHelper(database);

            PersonDao personDao = new PersonDao(database.openConnection());
            personDao.clearTable();

            System.out.println("Passed");

            System.out.println("Testing clear on empty Persons table");
            personDao.clearTable();
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    private void addPersonToDatabaseHelper(Database database) throws InternalServerException
    {
        PersonDao personDao = new PersonDao(database.openConnection());
        personDao.clearTable();

        String descendant = "Jeremy";
        String person_id = "blah";
        String first_name = "Jeremy";
        String last_name = "Van Patten";
        String gender = "m";

        Person p = new Person(descendant,person_id,first_name,last_name,gender);
        p.setFather("Dad");
        p.setMother("Mom");
        p.setSpouse("Wife");

        personDao.createPerson(p);
        database.closeConnection(true);
    }

    private void addMultiplePersonsToDatabaseHelper(Database database) throws InternalServerException
    {
        PersonDao personDao = new PersonDao(database.openConnection());
        personDao.clearTable();

        String descendant = "Jeremy";
        String person_id = "blah";
        String first_name = "Jeremy";
        String last_name = "Van Patten";
        String gender = "m";

        Person p = new Person(descendant,person_id,first_name,last_name,gender);
        p.setFather("Dad");
        p.setMother("Mom");
        p.setSpouse("Wife");
        personDao.createPerson(p);

        descendant = "Jeremy";
        person_id = "meh";
        first_name = "Bob";
        last_name = "Sagat";
        gender = "m";

        p = new Person(descendant,person_id,first_name,last_name,gender);
        p.setFather("Dad");
        p.setMother("Mom");
        p.setSpouse("Waifu");
        personDao.createPerson(p);

        database.closeConnection(true);
    }
}
