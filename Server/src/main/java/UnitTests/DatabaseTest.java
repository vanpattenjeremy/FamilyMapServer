package UnitTests;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

import DAO.Database;
import Exceptions.InternalServerException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseTest {

    @Test
    public void testOpenConnection()
    {
        System.out.println("Testing open connection");
        try {
            Database database = new Database();
            Connection conn = database.openConnection();
            assertNotNull(conn);
        }
        catch(InternalServerException e)
        {
            System.out.println(e.getMessage());
            Assert.fail();
        }
        System.out.println("Passed");
    }

    @Test
    public void testCloseConnection() {
        Database database = null;

        //Open a new database and make sure commit is working
        System.out.println("Testing close connection with commit");
        try {
            database = new Database();
            database.openConnection();
        } catch (InternalServerException e) {
            System.out.println("Failed to open");
            Assert.fail();
        }



        try {
            database.closeConnection(true);
        } catch (InternalServerException e) {
            Assert.fail();
        }
        System.out.println("Passed");
        System.out.println("Testing close connection with rollback");


        //Open database again and make sure rollback is working
        try {
            database.openConnection();
        } catch (InternalServerException e) {
            System.out.println("Failed to open second time");
            Assert.fail();
        }

        try {
            database.closeConnection(false);
        } catch (InternalServerException e)
        {
            assertEquals("Transaction rolled back",e.getMessage());
        }
        System.out.println("Passed");

    }

}
