package UnitTests;

import org.junit.Test;

import DAO.AuthTokenDao;
import DAO.Database;
import DAO.EventDao;
import DAO.PersonDao;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Result.ClearResult;
import Service.ClearService;

import static org.junit.Assert.assertEquals;


public class ClearTest {

    @Test
    public void testClear()
    {
        System.out.println("Testing clear on empty database");
        ClearService service = new ClearService();
        ClearResult result = service.clear();
        assertEquals("Clear succeeded", result.getMessage());
        System.out.println("Passed");
        System.out.println("Testing clear on a populated database");
        try {
            Database database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            EventDao eventDao = new EventDao(authTokenDao.getConn());
            PersonDao personDao = new PersonDao(authTokenDao.getConn());
            UserDao userDao = new UserDao(authTokenDao.getConn());

            String username = "Jeremy";
            String password = "Jeremy";
            String email = "jeremy@byu.edu";
            String first_name = "Jeremy";
            String last_name = "Van Patten";
            String gender = "m";
            String person_id = "bleh";
            String father = "Dad";
            String mother = "Mom";
            String spouse = "Wife";
        }
        catch(InternalServerException e)
        {

        }
    }
}
