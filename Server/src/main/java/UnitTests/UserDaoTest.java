package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import DAO.Database;
import DAO.UserDao;
import Exceptions.InternalServerException;
import Model.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class UserDaoTest {

    @Test
    public void testClearTable()
    {
        Connection conn = null;
        try{
            System.out.println("Testing clear on Users table with data");
            Database database = new Database();
            conn = database.openConnection();
            UserDao userDao = new UserDao(conn);

            userDao.clearTable();
            addUserToDatabaseHelper(conn);

            userDao.clearTable();

            System.out.println("Passed");

            System.out.println("Testing clear on empty Users table");
            userDao.clearTable();
            database.closeConnection(true);
        }
        catch(InternalServerException |SQLException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    @Test
    public void testGetUser()
    {
        Database database = null;
        Connection conn = null;
        UserDao userDao = null;
        String newID = null;
        try{
            System.out.println("Testing get user that is in database");
            database = new Database();
            conn = database.openConnection();
            userDao = new UserDao(conn);
            userDao.clearTable();

            addUserToDatabaseHelper(conn);

            database.closeConnection(true);
            conn.close();
        }
        catch(SQLException | InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        try{
            conn = database.openConnection();
            userDao = new UserDao(conn);
            User user = userDao.getUser("Jeremy");
            assertEquals("Jeremy",user.getUsername());
            assertEquals("Jeremy",user.getPassword());
            assertEquals("jeremy@byu.edu",user.getEmail());
            assertEquals("Jeremy", user.getFirst_name());
            assertEquals("Van Patten", user.getLast_name());
            assertEquals("m",user.getGender());
            assertEquals("blah", user.getPerson_id());
            database.closeConnection(true);
            conn.close();
        }
        catch(InternalServerException | SQLException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing get user that is not in the database");
            conn = database.openConnection();
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        try{
            userDao = new UserDao(conn);
            User user = userDao.getUser("Bob");
            if(user.getUsername().equals("Bob"))
            {
                database.closeConnection(true);
                conn.close();
                Assert.fail();
            }
        }
        catch(InternalServerException | SQLException e)
        {
            try{
                database.closeConnection(true);
                conn.close();
            }
            catch(InternalServerException | SQLException f)
            {
                System.out.println(f.getMessage());
                Assert.fail();
            }
        }
        System.out.println("Passed");
    }

    @Test
    public void testAddUser()
    {
        Database database = null;
        Connection conn = null;
        UserDao userDao = null;
        User u = null;
        try{
            System.out.println("Testing adding a user");
            String newID = UUID.randomUUID().toString();
            u = new User("Jeremy", "Jeremy", "jeremy@byu.edu", "Jeremy",
                    "Van Patten", "m", newID);
            database = new Database();
            conn = database.openConnection();
            userDao = new UserDao(conn);
            userDao.clearTable();

            userDao.addUser(u);
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
            userDao = new UserDao(conn);
            if(!u.equals(userDao.getUser("Jeremy")))
            {
                System.out.println("User objects do not match");
                database.closeConnection(true);
                conn.close();
                Assert.fail();
            }
            database.closeConnection(true);
            conn.close();
        }
        catch(InternalServerException | SQLException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");

        try{
            System.out.println("Testing adding an already existing user");
            conn = database.openConnection();
            userDao = new UserDao(conn);

            userDao.addUser(u);
        }
        catch(Exception e)
        {
            assertEquals("Failed to add new user to Users table",e.getMessage());
        }
        try{
            database.closeConnection(false);
            conn.close();
        }
        catch(InternalServerException | SQLException e)
        {
            System.out.println(e.getMessage());
        }
        System.out.println("Passed");
    }

    @Test
    public void testValidateUser()
    {
        Database database = null;
        Connection conn = null;
        UserDao userDao = null;

        try{
            System.out.println("Testing validating a user when there are no users in the Users table");
            database = new Database();
            conn = database.openConnection();
            userDao = new UserDao(conn);
            userDao.clearTable();
            boolean isValidated = userDao.validateUser("Jeremy", "Jeremy");
            database.closeConnection(true);
            assertFalse(isValidated);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing validating a user that is in the Users table");
            database = new Database();
            conn = database.openConnection();
            userDao = new UserDao(conn);
            addUserToDatabaseHelper(conn);
            database.closeConnection(true);
            conn = database.openConnection();
            userDao = new UserDao(conn);
            boolean isValidated = userDao.validateUser("Jeremy", "Jeremy");
            database.closeConnection(true);
            assertTrue(isValidated);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }

        System.out.println("Passed");

        try{
            System.out.println("Testing validating a user with the correct username, but incorrect password");
            database = new Database();
            conn = database.openConnection();
            userDao = new UserDao(conn);
            Boolean isValidated = userDao.validateUser("Jeremy", "fail");
            database.closeConnection(true);
            assertFalse(isValidated);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    private void addUserToDatabaseHelper(Connection conn) throws SQLException
    {
        String newID = UUID.randomUUID().toString();

        String sql = "INSERT INTO Users(username, password, email, first_name, last_name, gender, person_id) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1,"Jeremy");
        stmt.setString(2,"Jeremy");
        stmt.setString(3,"jeremy@byu.edu");
        stmt.setString(4,"Jeremy");
        stmt.setString(5,"Van Patten");
        stmt.setString(6,"m");
        stmt.setString(7,"blah");

        stmt.executeUpdate();
    }
}
