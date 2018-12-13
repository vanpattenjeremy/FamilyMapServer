package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Exceptions.InternalServerException;

public class Database {

    public Database() throws InternalServerException
    {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        }
        catch(ClassNotFoundException e)
        {
            throw new InternalServerException("Could not load driver");
        }
    }

    Connection conn = null;
    public Connection openConnection() throws InternalServerException
    {
        String dbName = "C:\\Users\\jerem\\AndroidStudioProjects\\FamilyMapServer\\Server\\FamilyMapDB.db";
        String connectionURL = "jdbc:sqlite:" + dbName;

        try {
            conn = DriverManager.getConnection(connectionURL);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new InternalServerException("Connection not opened " + connectionURL);
        }

        return conn;
    }

    public void closeConnection(boolean commit) throws InternalServerException
    {
        try{
            if(commit)
            {
                conn.commit();
                conn.close();
            }
            else
            {
                conn.rollback();
                conn.close();
            }
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Connection not closed");
        }
    }
}
