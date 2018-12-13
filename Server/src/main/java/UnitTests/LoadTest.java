package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;
import Service.ClearService;
import Service.LoadService;

import static org.junit.Assert.assertEquals;

public class LoadTest {

    @Test
    public void testLoad()
    {
        ClearService clearService = new ClearService();
        clearService.clear();

        LoadService loadService = new LoadService();
        ArrayList<User> users = new ArrayList<User>();
        ArrayList<Person> persons = new ArrayList<Person>();
        ArrayList<Event> events = new ArrayList<Event>();


        System.out.println("Testing adding a single valid user, person, and event");
        User u = new User("Jeremy", "Jeremy", "jeremy@byu.edu", "Jeremy", "Van Patten", "m", "blah");
        users.add(u);
        Person p = new Person("Jeremy", "blah", "Jeremy", "Van Patten", "m");
        persons.add(p);
        Event e = new Event("Jeremy", "blah", "blah", (float)10, (float)10, "USA", "provo", "born", 2000);
        events.add(e);
        LoadRequest request = new LoadRequest(users,persons,events);
        LoadResult result = loadService.load(request);

        assertEquals("Successfully added 1 users, 1 persons, and 1 events to the database.",result.getMessage());

        try {
            Database database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            PersonDao personDao = new PersonDao(userDao.getConn());
            EventDao eventDao = new EventDao(userDao.getConn());

            User user = userDao.getUser("Jeremy");
            assertEquals("Jeremy",user.getUsername());
            assertEquals("Jeremy",user.getPassword());
            assertEquals("jeremy@byu.edu",user.getEmail());
            assertEquals("Jeremy", user.getFirst_name());
            assertEquals("Van Patten", user.getLast_name());
            assertEquals("m",user.getGender());
            assertEquals("blah", user.getPerson_id());

            Person person = personDao.getOnePerson("Jeremy","blah");
            assertEquals("Jeremy", person.getDescendant());
            assertEquals("blah",person.getPerson_id());
            assertEquals("Jeremy", person.getFirst_name());
            assertEquals("Van Patten", person.getLast_name());
            assertEquals("m", person.getGender());

            Event event = eventDao.getOneEvent("Jeremy", "blah");
            database.closeConnection(true);
        }
        catch(Exception f)
        {
            System.out.println(f.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        System.out.println("Testing adding the same info again (should work because it clears beforehand)");
        loadService.load(request);

        try {
            Database database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            PersonDao personDao = new PersonDao(userDao.getConn());
            EventDao eventDao = new EventDao(userDao.getConn());

            User user = userDao.getUser("Jeremy");
            assertEquals("Jeremy",user.getUsername());
            assertEquals("Jeremy",user.getPassword());
            assertEquals("jeremy@byu.edu",user.getEmail());
            assertEquals("Jeremy", user.getFirst_name());
            assertEquals("Van Patten", user.getLast_name());
            assertEquals("m",user.getGender());
            assertEquals("blah", user.getPerson_id());

            Person person = personDao.getOnePerson("Jeremy","blah");
            assertEquals("Jeremy", person.getDescendant());
            assertEquals("blah",person.getPerson_id());
            assertEquals("Jeremy", person.getFirst_name());
            assertEquals("Van Patten", person.getLast_name());
            assertEquals("m", person.getGender());

            Event event = eventDao.getOneEvent("Jeremy", "blah");
            assertEquals("Jeremy", event.getDescendant());
            assertEquals("blah",event.getEvent_id());
            assertEquals("blah",event.getPerson_id());
            assertEquals(10.0,event.getLatitude(),0);
            assertEquals(10.0,event.getLongitude(),0);
            assertEquals("USA",event.getCountry());
            assertEquals("provo", event.getCity());
            assertEquals("born", event.getEvent_type());
            assertEquals(2000,event.getYear());
            database.closeConnection(true);
        }
        catch(Exception f)
        {
            System.out.println(f.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        System.out.println("Testing adding info with a repeated username input");
        u = new User("Jeremy", "Jeremy", "blah@byu.edu", "Natsu", "Dragneel", "m", "meh");
        users.add(u);
        request = new LoadRequest(users,persons,events);
        result = loadService.load(request);
        assertEquals("Failed to add new user to Users table",result.getMessage());
    }
}
