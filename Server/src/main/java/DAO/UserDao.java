package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Exceptions.InternalServerException;
import Exceptions.UsernameAlreadyTakenException;
import Model.User;

public class UserDao extends DaoInterface{

    public UserDao(Connection conn)
    {
        super(conn);
    }

    public void clearTable() throws InternalServerException
    {
        try{
            String sql = "DELETE FROM Users";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to clear Users table");
        }
    }

    public User getUser(String username) throws InternalServerException
    {
        try{
            String sql = "SELECT * FROM Users WHERE username = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,username);

            ResultSet rs = stmt.executeQuery();

            if(rs.next())
            {
                String password = rs.getString("password");
                String email = rs.getString("email");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String gender = rs.getString("gender");
                String person_id = rs.getString("person_id");
                return new User(username, password, email, first_name, last_name, gender, person_id);
            }
            else
            {
                throw new InternalServerException("Username does not exist in table");
            }
        }
        catch(SQLException e)
        {
            throw new InternalServerException("Failed to get the User from the table");
        }
    }

    public void addUser(User newUser) throws UsernameAlreadyTakenException
    {
        try{
            String sql = "INSERT INTO Users(username, password, email, first_name, last_name, gender, person_id) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,newUser.getUsername());
            stmt.setString(2,newUser.getPassword());
            stmt.setString(3,newUser.getEmail());
            stmt.setString(4,newUser.getFirst_name());
            stmt.setString(5,newUser.getLast_name());
            stmt.setString(6,newUser.getGender());
            stmt.setString(7,newUser.getPerson_id());

            stmt.executeUpdate();
        }
        catch(SQLException e)
        {
            throw new UsernameAlreadyTakenException("Username is already taken");
        }
    }

    public boolean validateUser(String username, String password) throws InternalServerException
    {
        try{
            String sql = "SELECT password FROM Users WHERE username = ?";
            PreparedStatement stmt = getConn().prepareStatement(sql);
            stmt.setString(1,username);

            ResultSet rs = stmt.executeQuery();

            if(!rs.next() || !password.equals(rs.getString("password")))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        catch(Exception e)
        {
            throw new InternalServerException("Failed to validate the user");
        }
    }
}
