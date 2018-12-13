package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Model.User;
import Request.FillRequest;
import Result.FillResult;
import Service.ClearService;
import Service.FillService;

import static org.junit.Assert.assertEquals;

public class FillTest {

    @Test
    public void testFill()
    {
        System.out.println("Testing fill with a username that does not exists in the database");
        ClearService clearService = new ClearService();
        clearService.clear();

        FillRequest request = new FillRequest("Jeremy");

        FillService fillService = new FillService();
        FillResult result = fillService.fill(request);

        assertEquals("Username does not exist in table", result.getMessage());

        System.out.println("Passed");

        try{
            System.out.println("Testing fill with a valid username and no generations parameter (should default to 4)");
            Database database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            User user = new User("Jeremy", "Jeremy", "jeremy@byu.edu", "Jeremy", "Van Patten", "m","blah");
            userDao.addUser(user);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        result = fillService.fill(request);

        assertEquals("Successfully added 31 persons and 116 events to the database.",result.getMessage());

        System.out.println("Passed");

        System.out.println("Testing fill with a valid username that already has data with a generations parameter");
        request.setGenerations(0);
        result = fillService.fill(request);

        assertEquals("Successfully added 1 persons and 2 events to the database.",result.getMessage());
        try {
            Database database = new Database();
            PersonDao personDao = new PersonDao(database.openConnection());
            EventDao eventDao = new EventDao(personDao.getConn());

            assertEquals(2, eventDao.getEvents("Jeremy").size());
            assertEquals(1,personDao.getPersons("Jeremy").size());
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

    }
}
