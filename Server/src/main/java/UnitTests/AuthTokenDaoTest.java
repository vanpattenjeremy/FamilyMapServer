package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.util.UUID;

import DAO.AuthTokenDao;
import DAO.Database;
import Exceptions.InternalServerException;
import Exceptions.InvalidAuthTokenException;
import Model.AuthToken;

import static org.junit.Assert.assertEquals;

public class AuthTokenDaoTest {

    @Test
    public void testGetUsername()
    {
        Database database = null;
        Connection conn = null;
        String newID = null;
        try{
            System.out.println("Testing get user that is in database");
            database = new Database();
            conn = database.openConnection();

            newID = addTokenToDatabaseHelper(conn);

            database.closeConnection(true);
            conn.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        try{
            conn = database.openConnection();
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            assertEquals("Jeremy",authTokenDao.getUsername(newID));
            database.closeConnection(true);
            conn.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try {
            System.out.println("Testing get user that is not in database");
            conn = database.openConnection();
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());

        }
        try{
            AuthTokenDao authTokenDao = new AuthTokenDao(conn);
            String user = authTokenDao.getUsername("j");
            if(user != null)
            {
                System.out.println("Found username with authToken that shouldn't exist");
                Assert.fail();
            }
        }
        catch(Exception e)
        {
            assertEquals("Auth_Token does not exist",e.getMessage());
        }
        finally
        {
            try{
                database.closeConnection(true);
                conn.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
                Assert.fail();
            }
        }
        System.out.println("Passed");
    }

    @Test
    public void testAddAuthToken()
    {
        Database database = null;
        String newToken = null;
        try{
            System.out.println("Testing adding a new Auth_Token");
            database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            authTokenDao.clearTable();

            newToken = UUID.randomUUID().toString();
            AuthToken authToken = new AuthToken(newToken, "Jeremy");
            authTokenDao.addAuthToken(authToken);
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        try{
            database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            assertEquals("Jeremy",authTokenDao.getUsername(newToken));
            database.closeConnection(true);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing adding an existing Auth_Token with different username");
            database = new Database();
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());

            AuthToken authToken = new AuthToken(newToken, "Bob");
            authTokenDao.addAuthToken(authToken);
        }
        catch(InternalServerException e)
        {
            assertEquals("Failed to add new authtoken to Auth_Tokens table", e.getMessage());
        }

        try {
            database.closeConnection(true);
        }
        catch (InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }


        try{
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            assertEquals("Jeremy", authTokenDao.getUsername(newToken));
            database.closeConnection(true);
        }
        catch(InternalServerException | InvalidAuthTokenException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding a second token to a user already in the table");
            AuthTokenDao authTokenDao = new AuthTokenDao(database.openConnection());
            newToken = UUID.randomUUID().toString();
            AuthToken authToken = new AuthToken(newToken, "Jeremy");
            authTokenDao.addAuthToken(authToken);
            assertEquals("Jeremy", authTokenDao.getUsername(newToken));
            database.closeConnection(true);
        }
        catch(InternalServerException | InvalidAuthTokenException e)
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
            System.out.println("Testing clear on Users table with data");
            Database database = new Database();
            AuthTokenDao authtokenDao = new AuthTokenDao(database.openConnection());

            authtokenDao.clearTable();
            addTokenToDatabaseHelper(authtokenDao.getConn());

            authtokenDao.clearTable();

            System.out.println("Passed");

            System.out.println("Testing clear on empty Users table");
            authtokenDao.clearTable();
            database.closeConnection(true);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    private String addTokenToDatabaseHelper(Connection conn) throws InternalServerException
    {

        AuthTokenDao authTokenDao = new AuthTokenDao(conn);
        authTokenDao.clearTable();

        String newAT = UUID.randomUUID().toString();

        AuthToken authToken = new AuthToken(newAT,"Jeremy");
        authTokenDao.addAuthToken(authToken);

        return newAT;
    }
}
