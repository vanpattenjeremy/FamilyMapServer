package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import DAO.Database;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;
import Service.ClearService;
import Service.LoginService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class LoginTest {

    @Test
    public void testLogin()
    {
        System.out.println("Testing logging in to an empty Users table");
        ClearService clearService = new ClearService();
        clearService.clear();

        LoginService loginService = new LoginService();
        LoginRequest request = new LoginRequest("Jeremy","Jeremy");
        LoginResult result = loginService.login(request);
        assertEquals("Incorrect username or password", result.getMessage());

        System.out.println("Passed");

        try{
            System.out.println("Testing logging in a user that exists in the Users table");
            Database database = new Database();
            UserDao userDao = new UserDao(database.openConnection());
            User user = new User("Jeremy","Jeremy","jeremy@byu.edu","Jeremy","Van Patten", "m", "blah");
            userDao.addUser(user);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        result = loginService.login(request);
        assertNotNull(result.getAuthToken());
        assertEquals("Jeremy",result.getUsername());
        assertEquals("blah",result.getPersonID());

        System.out.println("Passed");

        System.out.println("Testing logging in a user that exists with wrong password");
        request = new LoginRequest("Jeremy", "wrong");
        result = loginService.login(request);
        assertEquals("Incorrect username or password", result.getMessage());

        System.out.println("Passed");
    }
}
